using BackendServices.Models;

namespace BackendServices;

// Repositories/IProductRepository.cs
using System.Collections.Generic;
using System.Threading.Tasks;

public interface IProductRepository
{
    Task<IEnumerable<Product>> GetAllProducts();
    Task<Product> GetProductById(string productId);
    Task CreateProduct(Product product);
    Task UpdateProduct(Product product);
    Task DeleteProduct(string productId);
    Task ActivateProduct(string productId);
    Task DeactivateProduct(string productId);
}
