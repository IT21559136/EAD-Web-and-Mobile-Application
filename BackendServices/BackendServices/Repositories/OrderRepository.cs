using BackendServices.Models;
using MongoDB.Driver;

namespace BackendServices;

public class OrderRepository : IOrderRepository
{
    private readonly IMongoCollection<Order> _orderCollection;

    public OrderRepository(IMongoDatabase database)
    {
        _orderCollection = database.GetCollection<Order>("orders");
    }

    public async Task CreateOrderAsync(Order order)
    {
        await _orderCollection.InsertOneAsync(order);
    }

    public async Task<Order> GetOrderByIdAsync(string orderId)
    {
        return await _orderCollection.Find(o => o.Id == orderId).FirstOrDefaultAsync();
    }

    public async Task<List<Order>> GetOrdersByCustomerIdAsync(string customerId)
    {
        return await _orderCollection.Find(o => o.CustomerId == customerId).ToListAsync();
    }

    public async Task<List<Order>> GetOrdersByVendorEmailAsync(string vendorEmail)
    {
        return await _orderCollection.Find(o => o.Items.Any(i => i.VendorEmail == vendorEmail)).ToListAsync();
    }

    public async Task UpdateOrderAsync(Order order)
    {
        await _orderCollection.ReplaceOneAsync(o => o.Id == order.Id, order);
    }

    public async Task DeleteOrderAsync(string orderId)
    {
        await _orderCollection.DeleteOneAsync(o => o.Id == orderId);
    }
}
