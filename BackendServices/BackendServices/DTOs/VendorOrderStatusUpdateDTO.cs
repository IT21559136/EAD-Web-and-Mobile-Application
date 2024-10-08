namespace BackendServices.DTOs;

public class VendorOrderStatusUpdateDTO
{
    public string NewStatus { get; set; }  // "Not Ready", "Partially Delivered", "Delivered"
}
