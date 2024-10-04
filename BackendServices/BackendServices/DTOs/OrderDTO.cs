namespace BackendServices.DTOs;

public class OrderDTO
{
    public string CustomerId { get; set; }
    public List<OrderItemDTO> Items { get; set; }
    public string Note { get; set; } // For cancellation
}