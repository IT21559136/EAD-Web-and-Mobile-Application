// src/components/AppHeader.js
import React, { useState } from 'react';
import { Layout, Button, Badge, Dropdown, Menu, Tabs, List, Avatar } from 'antd';
import { MenuUnfoldOutlined, MenuFoldOutlined, BellOutlined, UserOutlined } from '@ant-design/icons';

const { Header } = Layout;
const { TabPane } = Tabs;

const AppHeader = ({ collapsed, setCollapsed }) => {
  const [notifications, setNotifications] = useState([
    { id: 1, content: 'New order received', read: false },
    { id: 2, content: 'Inventory low', read: false },
    { id: 3, content: 'New message from vendor', read: true },
  ]);

  const toggleCollapsed = () => {
    setCollapsed(!collapsed);
  };

  const markAsRead = (id) => {
    setNotifications((prevNotifications) =>
      prevNotifications.map((notif) =>
        notif.id === id ? { ...notif, read: true } : notif
      )
    );
  };

  const unreadNotifications = notifications.filter((notif) => !notif.read);
  const readNotifications = notifications.filter((notif) => notif.read);

  const notificationContent = (
    <div style={{ padding: 16, width: 300 }}>
      <Tabs defaultActiveKey="1">
        <TabPane tab={`Unread (${unreadNotifications.length})`} key="1">
          <List
            dataSource={unreadNotifications}
            renderItem={(notif) => (
              <List.Item
                actions={[
                  <Button type="link" onClick={() => markAsRead(notif.id)}>
                    Mark as read
                  </Button>,
                ]}
              >
                <List.Item.Meta
                  avatar={<Avatar icon={<BellOutlined />} />}
                  title={notif.content}
                />
              </List.Item>
            )}
          />
        </TabPane>
        <TabPane tab={`Read (${readNotifications.length})`} key="2">
          <List
            dataSource={readNotifications}
            renderItem={(notif) => (
              <List.Item>
                <List.Item.Meta
                  avatar={<Avatar icon={<BellOutlined />} />}
                  title={notif.content}
                />
              </List.Item>
            )}
          />
        </TabPane>
      </Tabs>
    </div>
  );

  const profileMenu = (
    <Menu>
      <Menu.Item key="1">Profile</Menu.Item>
      <Menu.Item key="2">Settings</Menu.Item>
      <Menu.Item key="3">Logout</Menu.Item>
    </Menu>
  );

  return (
    <Header className="site-layout-background" style={{ padding: 0, background: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
      <div>
        <Button
          type="primary"
          onClick={toggleCollapsed}
          style={{ margin: '16px' }}
        >
          {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
        </Button>
        <span style={{ fontSize: '1.5rem', marginLeft: '16px' }}>My Web Application</span>
      </div>
      <div style={{ marginRight: '16px' }}>
        <Dropdown overlay={<Menu>{notificationContent}</Menu>} trigger={['click']} placement="bottomRight">
          <Badge count={unreadNotifications.length}>
            <BellOutlined style={{ fontSize: '18px', cursor: 'pointer', marginRight: '16px' }} />
          </Badge>
        </Dropdown>
        <Dropdown overlay={profileMenu} trigger={['click']} placement="bottomRight">
          <Avatar icon={<UserOutlined />} style={{ cursor: 'pointer' }} />
        </Dropdown>
      </div>
    </Header>
  );
};

export default AppHeader;
