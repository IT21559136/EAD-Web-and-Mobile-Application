﻿// using BackendServices.DTOs;
// using BackendServices.Models;
// using MongoDB.Driver;
//
// namespace BackendServices.Controllers;
//
// // Controllers/AdminController.cs
// using Microsoft.AspNetCore.Authorization;
// using Microsoft.AspNetCore.Mvc;
//
// [ApiController]
// [Route("api/[controller]")]
// //[Authorize(Roles = "Administrator")]
// public class AdminController : ControllerBase
// {
//     
//     private readonly IMongoCollection<User> _users;
//
//     // Inject MongoDB database or collection through the constructor
//     public AdminController(IMongoDatabase database)
//     {
//         // Assuming the "Users" collection is stored in the database
//         _users = database.GetCollection<User>("Users");
//     }
//     
//     
//     // Only Administrator can access this
//     [HttpPost("create-vendor")]
//     public async Task<IActionResult> CreateVendor([FromBody] RegisterDTO vendorDto)
//     {
//         var hashedPassword = BCrypt.Net.BCrypt.HashPassword(vendorDto.Password);
//
//         var user = new User
//         {
//             Username = vendorDto.Username,
//             PasswordHash = hashedPassword,
//             Role = "Vendor" // Assign the Vendor role
//         };
//
//         await _users.InsertOneAsync(user);
//
//         return Ok("Vendor created successfully.");
//     }
//     
//     
//     [HttpPost("create-csr")]
//     public async Task<IActionResult> CreateCsr([FromBody] RegisterDTO csrDto)
//     {
//         var hashedPassword = BCrypt.Net.BCrypt.HashPassword(csrDto.Password);
//
//         var user = new User
//         {
//             Username = csrDto.Username,
//             PasswordHash = hashedPassword,
//             Role = "CSR" // Assign CSR role
//         };
//
//         await _users.InsertOneAsync(user);
//
//         return Ok("CSR created successfully.");
//     }
//
// }
