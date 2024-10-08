package com.example.mobile_application.feature_profile.presentation

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile_application.R
import com.example.mobile_application.core.presentation.ui.theme.MainWhiteColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    var name by remember { mutableStateOf(TextFieldValue("Jana")) }
    var email by remember { mutableStateOf(TextFieldValue("jana@gmail.com")) }
    var phone by remember { mutableStateOf(TextFieldValue("+1234567890")) }
    var address by remember { mutableStateOf(TextFieldValue("123 Main St, City")) } // New address state
    var modified by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) } // Track editing state

    // Dropdown menu state
    var expanded by remember { mutableStateOf(false) }

    // Function to check if any field has changed
    fun checkForChanges(newName: TextFieldValue, newEmail: TextFieldValue, newPhone: TextFieldValue, newAddress: TextFieldValue) {
        modified = newName.text != name.text || newEmail.text != email.text || newPhone.text != phone.text || newAddress.text != address.text
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainWhiteColor
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "My Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu), // Use your menu icon
                            contentDescription = "Menu"
                        )
                    }

                    // Dropdown Menu
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(onClick = {
                            // Handle Logout action
                            expanded = false
                        },
                            text = { Text("Logout") }
                        )
                        DropdownMenuItem(onClick = {
                            // Handle Deactivate action
                            expanded = false
                        },
                            text = { Text("Deactivate") }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circle with First Letter of Name
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), shape = CircleShape)
                    .clickable { /* Change picture action */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.text.first().toString(), // Display the first letter of the name
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Editable Name
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    checkForChanges(it, email, phone, address) // Include address in change check
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing // Toggle enabled state based on isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Editable Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    checkForChanges(name, it, phone, address) // Include address in change check
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing // Toggle enabled state based on isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Editable Phone
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    checkForChanges(name, email, it, address) // Include address in change check
                },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing // Toggle enabled state based on isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Editable Address
            OutlinedTextField(
                value = address,
                onValueChange = {
                    address = it
                    checkForChanges(name, email, phone, it) // Include address in change check
                },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditing // Toggle enabled state based on isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Button
            Button(
                onClick = {
                    if (isEditing) {
                        // If already editing, reset modified state
                        modified = false
                    }
                    isEditing = !isEditing // Toggle editing state
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Save" else "Edit") // Change button text based on editing state
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Conditional Buttons for Update and Cancel
            if (modified) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            // Handle Update action
                            // Reset modified state after update
                            modified = false
                        },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Update")
                    }

                    Button(
                        onClick = {
                            // Reset fields to original values
                            name = TextFieldValue("Jana")
                            email = TextFieldValue("jana@gmail.com")
                            phone = TextFieldValue("+1234567890")
                            address = TextFieldValue("123 Main St, City") // Reset address
                            modified = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }
}
