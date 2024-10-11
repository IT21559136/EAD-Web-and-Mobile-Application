using BackendServices.Configurations;
using BackendServices.Models;
using Microsoft.Extensions.Options;

namespace BackendServices;

// Repositories/UserRepository.cs
using MongoDB.Driver;
using System.Collections.Generic;
using System.Threading.Tasks;

//This class implements the IUserRepository interface, providing data access methods for user management using MongoDB.
public class UserRepository : IUserRepository
{
    private readonly IMongoCollection<User> _users;

    public UserRepository(IOptions<MongoDBSettings> settings)
    {
        var client = new MongoClient(settings.Value.ConnectionString);
        var database = client.GetDatabase(settings.Value.DatabaseName);
        _users = database.GetCollection<User>("Users");
    }

    //Retrieves a user from the MongoDB collection based on their email address.
    public async Task<User> GetUserByEmailAsync(string email)
    {
        return await _users.Find(user => user.Email == email).FirstOrDefaultAsync();
    }

    //Inserts a new user document into the MongoDB collection.
    public async Task CreateUserAsync(User user)
    {
        await _users.InsertOneAsync(user);
    }

    //Updates an existing user document in the MongoDB collection by replacing it with the provided user object.
    public async Task UpdateUserAsync(User user)
    {
        await _users.ReplaceOneAsync(u => u.Id == user.Id, user);
    }

    //Checks if the specified email address is unique by verifying that no user with that email exists in the collection.
    public async Task<bool> IsEmailUniqueAsync(string email)
    {
        return await _users.Find(u => u.Email == email).AnyAsync() == false;
    }

    //Deletes a user document from the MongoDB collection based on the specified user ID.
    public async Task DeleteUserAsync(string userId)
    {
        await _users.DeleteOneAsync(u => u.Id == userId);
    }

    //Retrieves a list of all users from the MongoDB collection.
    public async Task<List<User>> GetAllUsersAsync()
    {
        return await _users.Find(user => true).ToListAsync();
    }

    //Retrieves a user document from the MongoDB collection based on the specified user ID.
    public async Task<User> GetUserByIdAsync(string id)
    {
        return await _users.Find(user => user.Id == id).FirstOrDefaultAsync();
    }
}
