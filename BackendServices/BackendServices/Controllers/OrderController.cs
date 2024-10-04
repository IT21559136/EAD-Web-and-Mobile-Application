using BackendServices.DTOs;
using BackendServices.Services;
using Microsoft.AspNetCore.Mvc;

namespace BackendServices.Controllers;

[ApiController]
[Route("api/[controller]")]
public class OrderController : ControllerBase
{
    private readonly OrderService _orderService;

    public OrderController(OrderService orderService)
    {
        _orderService = orderService;
    }

    // Create a new order
    [HttpPost("create")]
    public async Task<IActionResult> CreateOrder([FromBody] OrderDTO orderDto)
    {
        await _orderService.CreateOrderAsync(orderDto);
        return Ok("Order created successfully.");
    }

    // Get all orders for a customer
    [HttpGet("customer/{customerId}")]
    public async Task<IActionResult> GetOrdersByCustomer(string customerId)
    {
        var orders = await _orderService.GetOrdersByCustomerAsync(customerId);
        return Ok(orders);
    }

    // Get orders for a specific vendor
    [HttpGet("vendor/{vendorEmail}")]
    public async Task<IActionResult> GetOrdersByVendor(string vendorEmail)
    {
        var orders = await _orderService.GetOrdersByVendorAsync(vendorEmail);
        return Ok(orders);
    }

    // Update the status of a product by a vendor
    [HttpPut("update-status/{orderId}/{productId}")]
    public async Task<IActionResult> UpdateProductStatus(string orderId, string productId, [FromQuery] string vendorEmail, [FromQuery] string status)
    {
        await _orderService.UpdateProductStatusAsync(orderId, productId, vendorEmail, status);
        return Ok("Product status updated successfully.");
    }

    // Cancel an order by customer or admin
    [HttpPut("cancel/{orderId}")]
    public async Task<IActionResult> CancelOrder(string orderId, [FromBody] string note)
    {
        await _orderService.CancelOrderAsync(orderId, note);
        return Ok("Order cancelled successfully.");
    }

    // Admin deletes an order
    [HttpDelete("{orderId}")]
    public async Task<IActionResult> DeleteOrder(string orderId)
    {
        await _orderService.DeleteOrderAsync(orderId);
        return Ok("Order deleted successfully.");
    }
}
