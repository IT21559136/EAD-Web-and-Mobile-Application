namespace BackendServices.Models;

public class Order
{
    public string Id { get; set; }
    public string CustomerId { get; set; } // The user who placed the order
    public List<OrderItem> Items { get; set; } // The products in the order
    public decimal TotalAmount { get; set; } // Total price of the order
    public string Status { get; set; } // Order status (e.g., processing, partially_delivered, delivered, cancelled)
    public DateTime CreatedDate { get; set; } 
    public DateTime? DispatchedDate { get; set; } // Date when dispatched
    public string Note { get; set; } // Reason for cancellation
}