import { headerStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import { HighlightOffOutlined } from "@mui/icons-material";
import {
  Box,
  Button,
  Card,
  IconButton,
  Modal,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import UserItem from "./UserItem";
import { useEffect } from "react";
import { User } from "@/constants/Types";
import React from "react";

interface UserListModalProps {
  isAdminModalOpen: boolean;
  setIsAdminModalOpen: (isOpen: boolean) => void;
}

const UserListModal = (props: UserListModalProps) => {
  const palette = paletteProvider();
  const [users, setUsers] = React.useState<User[]>([]);

  const modalStyle = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: "60%",
    "@media (min-width:450px)": {
      width: "50%",
    },
    "@media (min-width:600px)": {
      width: "40%",
    },
    "@media (min-width:1050px)": {
      width: "40%",
    },
    bgcolor: palette.primary.light,
    p: "2rem",
    borderRadius: "1.25rem",
  };

  const tableRowStyle = {
    backgroundColor: palette.primary.main,
    color: palette.text.secondary,
    borderRadius: "1rem",
    '&:last-child td, &:last-child th': { 
        borderBottom: 'none', 
        borderRadius: '0 0 1rem 1rem', 
        paddingBottom: '1.5rem',
      },
  };

  const tableHeaderStyle = {
    borderBottom: `2px solid ${palette.text.secondary}`,
    color: palette.text.secondary,
    '& th:last-child': {
      position: "relative",
      "&::after": {
        content: '""',
        display: 'none',
      },
    },
  };

  const idCellStyle = {
    borderBottom: "none",
    textAlign: 'center',
    verticalAlign: 'middle',
    position: "relative",
    color: palette.text.secondary,
    "&::after": {
      content: '""',
      position: "absolute",
      right: 0,
      top: "20%",
      bottom: "20%",
      width: "2px",
      backgroundColor: palette.text.secondary,
      borderRadius: "1rem",
    },
  };

  const handleClosePasswordModal = () => {
    props.setIsAdminModalOpen(false);
  };

  const handleUserStatusChange = (userId: number, isBanned: boolean) => {
    const updatedUsers = users.map(user =>
      user.id === userId ? { ...user, isBanned } : user
    );
    setUsers(updatedUsers);
  };

  const fetchUsers = async (): Promise<User[]> => {
    const token = sessionStorage.getItem("token");
    const url = `http://localhost:8080/api/user/allUsers/withBlocked`;
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };
  
    try {
      const response = await fetch(url, requestOptions);
      if (response.ok) {
        return response.json();
      } else {
        throw new Error("Something went wrong");
      }
    } catch (error) {
      console.error(error);
      return [];
    }
  }

  useEffect(() => {
    const getUsers = async () => {
      const fetchedUsers = await fetchUsers();
      setUsers(fetchedUsers);
    };

    if(props.isAdminModalOpen){
      getUsers();
    }
  }, [[props.isAdminModalOpen]]);



  return (
    <Modal
      open={props.isAdminModalOpen}
      onClose={handleClosePasswordModal}
      aria-labelledby="change-password-modal"
      aria-describedby="change-password-form"
    >
      <Card sx={modalStyle}>
        <Box sx={headerStyle("space-between")}>
          <Typography
            fontWeight={"bold"}
            fontSize={"1.25rem"}
            marginLeft={"45%"}
            textAlign={"center"}
          >
            User list
          </Typography>
          <IconButton size="medium" onClick={handleClosePasswordModal}>
            <HighlightOffOutlined
              fontSize="large"
              sx={{ color: palette.primary.main }}
            />
          </IconButton>
        </Box>
        <Table sx={tableRowStyle}>

          <TableHead sx={tableHeaderStyle}>
            <TableRow >
              <TableCell sx={idCellStyle}>Id</TableCell>
              <TableCell sx={idCellStyle}>
               Full name
              </TableCell>
              <TableCell sx={idCellStyle}>Email</TableCell>
              <TableCell sx={idCellStyle}>Ban/unban</TableCell>
            </TableRow>
          </TableHead>

          <TableBody>
            {users.map((user) => (
              <UserItem user={user} key={user.id} handleUserStatusChange={handleUserStatusChange} />
            ))}
          </TableBody>
        </Table>
      </Card>
    </Modal>
  );
};

export default UserListModal;
