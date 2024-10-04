namespace BackendServices.DTOs;

public class OrderDTO
{
    public string CustomerId { get; set; }
    public List<OrderItemDTO> Items { get; set; }
    public string Note { get; set; } // For cancellation
}

public class OrderItemDTO
{
    public string ProductId { get; set; }
    public int Quantity { get; set; }
}
