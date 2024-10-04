using BackendServices.DTOs;
using BackendServices.Models;

namespace BackendServices.Services;

public class OrderService
{
    private readonly IOrderRepository _orderRepository;
    private readonly UserService _userService; // For notifications

    public OrderService(IOrderRepository orderRepository, UserService userService)
    {
        _orderRepository = orderRepository;
        _userService = userService;
    }

    // Create a new order
    public async Task CreateOrderAsync(OrderDTO orderDto)
    {
        var order = new Order
        {
            CustomerId = orderDto.CustomerId,
            Items = orderDto.Items.Select(itemDto => new OrderItem
            {
                ProductId = itemDto.ProductId,
                Quantity = itemDto.Quantity,
                ItemStatus = "processing"
            }).ToList(),
            TotalAmount = 0, // Calculation of total will be done here
            Status = "processing",
            CreatedDate = DateTime.UtcNow
        };

        // Notify vendor about the new order
        foreach (var item in order.Items)
        {
            await _userService.NotifyVendorAsync(item.VendorEmail, "New order placed for your product.");
        }

        await _orderRepository.CreateOrderAsync(order);
    }

    // Get all orders for a customer
    public async Task<List<Order>> GetOrdersByCustomerAsync(string customerId)
    {
        return await _orderRepository.GetOrdersByCustomerIdAsync(customerId);
    }

    // Get orders for a specific vendor
    public async Task<List<Order>> GetOrdersByVendorAsync(string vendorEmail)
    {
        return await _orderRepository.GetOrdersByVendorEmailAsync(vendorEmail);
    }

    // Vendor updates the status of their product in the order
    public async Task UpdateProductStatusAsync(string orderId, string productId, string vendorEmail, string status)
    {
        var order = await _orderRepository.GetOrderByIdAsync(orderId);
        var item = order.Items.FirstOrDefault(i => i.ProductId == productId && i.VendorEmail == vendorEmail);

        if (item != null)
        {
            item.ItemStatus = status;

            // Check if all items are delivered to update the overall order status
            if (order.Items.All(i => i.ItemStatus == "delivered"))
            {
                order.Status = "delivered";
                await _userService.NotifyCustomerAsync(order.CustomerId, "Your order has been fully delivered.");
            }
            else if (order.Items.Any(i => i.ItemStatus == "delivered"))
            {
                order.Status = "partially_delivered";
            }

            await _orderRepository.UpdateOrderAsync(order);
        }
    }

    // Cancel an order by customer or admin
    public async Task CancelOrderAsync(string orderId, string note)
    {
        var order = await _orderRepository.GetOrderByIdAsync(orderId);
        if (order != null && order.Status != "delivered")
        {
            order.Status = "cancelled";
            order.Note = note;
            await _orderRepository.UpdateOrderAsync(order);

            // Notify customer
            await _userService.NotifyCustomerAsync(order.CustomerId, "Your order has been cancelled.");
        }
    }

    // Admin deletes an order
    public async Task DeleteOrderAsync(string orderId)
    {
        await _orderRepository.DeleteOrderAsync(orderId);
    }
}
