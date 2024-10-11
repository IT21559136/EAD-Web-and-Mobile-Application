using BackendServices.Models;

namespace BackendServices;

// Repositories/IUserRepository.cs
using System.Collections.Generic;
using System.Threading.Tasks;

//This interface defines the contract for user data access operations, outlining the methods that any user repository implementation must provide.
public interface IUserRepository
{
    Task<User> GetUserByEmailAsync(string email);
    Task CreateUserAsync(User user);
    Task UpdateUserAsync(User user);
    Task<bool> IsEmailUniqueAsync(string email);
    Task DeleteUserAsync(string userId);
    Task<List<User>> GetAllUsersAsync();
    Task<User> GetUserByIdAsync(string id);
}
