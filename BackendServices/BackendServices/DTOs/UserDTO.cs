namespace BackendServices.DTOs;

public class UserDTO
{
    public string Username { get; set; }
    public string Email { get; set; }
    public string Role { get; set; }  // Administrator, Vendor, CSR, etc.
    public string Password { get; set; }  // Optional: Only update if provided
    
    public int Status { get; set; } 
}
