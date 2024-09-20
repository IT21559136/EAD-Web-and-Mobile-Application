// src/components/UserManagement.js
import React, { useState } from 'react';
import { Table, Button, Form, Input, Select, Switch, Space, Modal } from 'antd';
import { PlusOutlined, EditOutlined, SearchOutlined } from '@ant-design/icons';

const { Option } = Select;

const initialUsers = [
  { key: '1', fullName: 'John Doe', email: 'john@example.com', role: 'Administrator', status: true },
  { key: '2', fullName: 'Jane Smith', email: 'jane@example.com', role: 'Vendor', status: false },
  { key: '3', fullName: 'Michael Brown', email: 'michael@example.com', role: 'CSR', status: true },
];

const UserManagement = () => {
  const [users, setUsers] = useState(initialUsers);
  const [editingKey, setEditingKey] = useState('');
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [addForm] = Form.useForm();
  const [editForm] = Form.useForm();
  const [searchText, setSearchText] = useState('');

  // Filter users by search text
  const filteredUsers = users.filter((user) => user.fullName.toLowerCase().includes(searchText.toLowerCase()));

  // Table Columns for Users with Editing
  const columns = [
    {
      title: 'Full Name',
      dataIndex: 'fullName',
      key: 'fullName',
      sorter: (a, b) => a.fullName.localeCompare(b.fullName),
      render: (_, record) =>
        record.key === editingKey ? (
          <Form.Item
            name="fullName"
            style={{ margin: 0 }}
            rules={[{ required: true, message: 'Please enter the full name' }]}
          >
            <Input placeholder="Enter full name" />
          </Form.Item>
        ) : (
          record.fullName
        ),
    },
    {
      title: 'Email Address',
      dataIndex: 'email',
      key: 'email',
      sorter: (a, b) => a.email.localeCompare(b.email),
      render: (_, record) =>
        record.key === editingKey ? (
          <Form.Item
            name="email"
            style={{ margin: 0 }}
            rules={[{ required: true, message: 'Please enter a valid email address' }]}
          >
            <Input placeholder="Enter email address" />
          </Form.Item>
        ) : (
          record.email
        ),
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
      sorter: (a, b) => a.role.localeCompare(b.role),
      render: (_, record) =>
        record.key === editingKey ? (
          <Form.Item name="role" style={{ margin: 0 }} rules={[{ required: true, message: 'Please select a role!' }]}>
            <Select placeholder="Select Role">
              <Option value="Administrator">Administrator</Option>
              <Option value="Vendor">Vendor</Option>
              <Option value="CSR">CSR</Option>
            </Select>
          </Form.Item>
        ) : (
          record.role
        ),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      sorter: (a, b) => a.status - b.status,
      render: (_, record) =>
        record.key === editingKey ? (
          <Form.Item name="status" style={{ margin: 0 }} valuePropName="checked">
            <Switch />
          </Form.Item>
        ) : record.status ? (
          'Active'
        ) : (
          'Inactive'
        ),
    },
    {
      title: 'Action',
      key: 'action',
      render: (_, record) =>
        record.key === editingKey ? (
          <Space>
            <Button onClick={() => save(record.key)} type="primary">
              Save
            </Button>
            <Button onClick={cancel}>Cancel</Button>
          </Space>
        ) : (
          <Button onClick={() => edit(record)} icon={<EditOutlined />} />
        ),
    },
  ];

  // Edit User
  const edit = (record) => {
    editForm.setFieldsValue({ fullName: '', email: '', role: '', status: '', ...record });
    setEditingKey(record.key);
  };

  // Save Edited User
  const save = async (key) => {
    try {
      const row = await editForm.validateFields();
      const newData = [...users];
      const index = newData.findIndex((item) => key === item.key);

      if (index > -1) {
        const item = newData[index];
        newData.splice(index, 1, { ...item, ...row });
        setUsers(newData);
        setEditingKey('');
      }
    } catch (errInfo) {
      console.log('Validate Failed:', errInfo);
    }
  };

  const cancel = () => {
    setEditingKey('');
  };

  // Add New User Modal
  const handleAddUser = () => {
    addForm
      .validateFields()
      .then((values) => {
        const newUser = {
          key: `${users.length + 1}`,
          ...values,
          status: values.status ? true : false,
        };
        setUsers([...users, newUser]);
        setIsModalVisible(false);
        addForm.resetFields();
      })
      .catch((info) => {
        console.log('Validate Failed:', info);
      });
  };

  // Search handler
  const onSearch = (e) => {
    setSearchText(e.target.value);
  };

  return (
    <>
      <Space style={{ marginBottom: 16 }}>
        <Button onClick={() => setIsModalVisible(true)} type="primary" icon={<PlusOutlined />}>
          Add New User
        </Button>
        <Input
          prefix={<SearchOutlined />}
          placeholder="Search by full name"
          value={searchText}
          onChange={onSearch}
        />
      </Space>
      <Form form={editForm} component={false}>
        <Table
          dataSource={filteredUsers}
          columns={columns}
          rowClassName="editable-row"
          pagination={{ pageSize: 5 }}
          onChange={(pagination, filters, sorter) => {
            console.log('params', pagination, filters, sorter);
          }}
        />
      </Form>

      {/* Modal for Adding New User */}
      <Modal
        title="Add New User"
        visible={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        onOk={handleAddUser}
      >
        <Form form={addForm} layout="vertical">
          <Form.Item
            name="fullName"
            label="Full Name"
            rules={[{ required: true, message: 'Please enter full name' }]}
          >
            <Input placeholder="Enter full name" />
          </Form.Item>
          <Form.Item
            name="email"
            label="Email Address"
            rules={[{ required: true, message: 'Please enter an email address' }]}
          >
            <Input placeholder="Enter email" />
          </Form.Item>
          <Form.Item name="role" label="Role" rules={[{ required: true, message: 'Please select a role' }]}>
            <Select placeholder="Select Role">
              <Option value="Administrator">Administrator</Option>
              <Option value="Vendor">Vendor</Option>
              <Option value="CSR">CSR</Option>
            </Select>
          </Form.Item>
          <Form.Item name="status" label="Status" valuePropName="checked">
            <Switch checkedChildren="Active" unCheckedChildren="Inactive" />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default UserManagement;
