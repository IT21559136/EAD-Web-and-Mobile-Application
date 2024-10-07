import React, { useContext, useState } from 'react';
import { Navbar, Button, Dropdown, DropdownButton, Badge } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext'; // Import AuthContext

const Header = ({ toggleSidebar }) => {
  const [notifications, setNotifications] = useState([
    { id: 1, text: 'New user signed up', read: false },
    { id: 2, text: 'Inventory running low', read: true },
    { id: 3, text: 'New order placed', read: false }
  ]);

  const [profileData, setProfileData] = useState({
    name: 'John Doe',
    email: 'john.doe@example.com'
  });

  const { logout } = useContext(AuthContext); // Get logout from AuthContext
  const navigate = useNavigate();

  const fetchNotifications = () => {
    // Mock API call to fetch notifications
    setTimeout(() => {
      setNotifications([
        { id: 4, text: 'New message received', read: false },
        { id: 5, text: 'Update system', read: true }
      ]);
    }, 500);
  };

  const fetchProfileData = () => {
    // Mock API call to fetch profile data
    setTimeout(() => {
      setProfileData({
        name: 'Jane Smith',
        email: 'jane.smith@example.com'
      });
    }, 500);
  };

  const toggleReadStatus = (id) => {
    setNotifications(
      notifications.map((notif) =>
        notif.id === id ? { ...notif, read: !notif.read } : notif
      )
    );
  };

  const handleLogout = () => {
    logout(); // Call the logout function from AuthContext
    navigate('/login'); // Redirect to login page after logout
  };

  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="px-3">
      <Button variant="outline-light" onClick={toggleSidebar}>
        <i className="fas fa-bars"></i>
      </Button>
      <Navbar.Brand className="mx-3">
        <Link to="/" style={{ color: 'white', textDecoration: 'none' }}>
          Admin Dashboard
        </Link>
      </Navbar.Brand>

      <div className="ml-auto d-flex" style={{ marginLeft: 'auto' }}>
        {/* Notification Icon */}
        <DropdownButton
          variant="outline-light"
          title={
            <>
              <i className="fas fa-bell"></i>
              {notifications.some((notif) => !notif.read) && (
                <Badge bg="danger" className="ms-1">
                  {notifications.filter((notif) => !notif.read).length}
                </Badge>
              )}
            </>
          }
          className="mx-2"
          onClick={fetchNotifications}
          align="end"
        >
          <Dropdown.Header>Notifications</Dropdown.Header>
          {notifications.map((note) => (
            <Dropdown.Item
              key={note.id}
              onClick={() => toggleReadStatus(note.id)}
              style={{ fontWeight: note.read ? 'normal' : 'bold' }}
            >
              {note.text}
              {note.read ? '' : <Badge bg="success" className="ms-2">New</Badge>}
            </Dropdown.Item>
          ))}
        </DropdownButton>

        {/* Profile Icon */}
        <DropdownButton
          variant="outline-light"
          title={<i className="fas fa-user"></i>}
          className="mx-2"
          onClick={fetchProfileData}
          align="end"
        >
          <Dropdown.Header>Profile</Dropdown.Header>
          <Dropdown.Item>Name: {profileData.name}</Dropdown.Item>
          <Dropdown.Item>Email: {profileData.email}</Dropdown.Item>
          <Dropdown.Divider />
          <Dropdown.Item onClick={handleLogout} className="text-danger">
            <i className="fas fa-sign-out-alt"></i> Logout
          </Dropdown.Item>
        </DropdownButton>
      </div>
    </Navbar>
  );
};

export default Header;
