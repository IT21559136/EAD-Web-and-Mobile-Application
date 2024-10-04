using BackendServices.Models;

namespace BackendServices;

public interface IOrderRepository
{
    Task CreateOrderAsync(Order order);
    Task<Order> GetOrderByIdAsync(string orderId);
    Task<List<Order>> GetOrdersByCustomerIdAsync(string customerId);
    Task<List<Order>> GetOrdersByVendorEmailAsync(string vendorEmail);
    Task UpdateOrderAsync(Order order);
    Task DeleteOrderAsync(string orderId);
}
