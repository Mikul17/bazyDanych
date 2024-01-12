import { User } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Button,TableCell, TableRow } from "@mui/material";
import React from "react";

interface UserItemProps {
  user: User;
  handleUserStatusChange: (userId: number, isBanned: boolean) => void;
}

const UserItem = (props: UserItemProps) => {
  const palette = paletteProvider();
  const [isBanned, setIsBanned] = React.useState<boolean>(props.user.isBanned || false);

  const tableRowStyle = {
    backgroundColor: palette.primary.main,
    color: palette.text.secondary,
    margin: "0.5rem",
    borderRadius: "1rem",
  };

  const idCellStyle = {
    textAlign: "center",
    verticalAlign: "middle",
    borderBottom: "none",
    color: palette.text.secondary,
    position: "relative",
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

  const banUser = () => {
    const token = sessionStorage.getItem("token");
    const url = `http://localhost:8080/api/user/ban?userId=${props.user.id}`;
    const requestOptions = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };

    try {
      fetch(url, requestOptions).then((response) => {
        if (response.ok) {
          setIsBanned(true);
        } else {
          throw new Error("Something went wrong");
        }
      });
    } catch (error) {
      console.log(error);
    }
  };

  const unbanUser = () => {
    const token = sessionStorage.getItem("token");
    const url = `http://localhost:8080/api/user/unban?userId=${props.user.id}`;
    const requestOptions = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    };

    try {
      fetch(url, requestOptions).then((response) => {
        if (response.ok) {
          setIsBanned(false);
        } else {
          throw new Error("Something went wrong");
        }
      });
    } catch (error) {
      console.log(error);
    }
  };

  const handleUserBan = () => {
    banUser();
    setIsBanned(true);
    props.handleUserStatusChange(props.user.id, isBanned);
  };

  const handleUserUnban = () => {
    unbanUser();
    setIsBanned(false);
    props.handleUserStatusChange(props.user.id, isBanned);
  };




  return (
    <TableRow sx={tableRowStyle} key={props.user.id}>
      <TableCell sx={idCellStyle}>{props.user.id}</TableCell>
      <TableCell sx={idCellStyle}>
        {props.user.firstName} {props.user.lastName}
      </TableCell>
      <TableCell sx={idCellStyle}>{props.user.email}</TableCell>
      <TableCell
        sx={{
          borderBottom: "none",
          textAlign: "center",
          verticalAlign: "middle",
        }}
      >
        {!isBanned ? (
          <Button
            variant="contained"
            color="error"
            onClick={handleUserBan}
            sx={{ borderRadius: "0.75rem" }}
          >
            Ban
          </Button>
        ) : (
          <Button
            variant="contained"
            color="info"
            onClick={handleUserUnban}
            sx={{ borderRadius: "0.75rem" }}
          >
            Unban
          </Button>
        )}
      </TableCell>
    </TableRow>
  );
};

export default UserItem;
