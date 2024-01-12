import { buttonStyle, headerStyle, inputStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import { HighlightOffOutlined, TextFieldsOutlined } from "@mui/icons-material";
import {
  Alert,
  Box,
  Button,
  IconButton,
  Modal,
  TextField,
  Typography,
} from "@mui/material";
import { jwtDecode } from "jwt-decode";
import { useState } from "react";

interface ChangePasswordModalProps {
  isPasswordModalOpen: boolean;
  setPasswordModalOpen: (isOpen: boolean) => void;
}

const ChangePasswordModal = (props: ChangePasswordModalProps) => {
  const palette = paletteProvider();

  const [oldPassword, setOldPassword] = useState<string>("");
  const [newPassword, setNewPassword] = useState<string>("");

  const [alertState, setAlertState] = useState<{
    message: string;
    type: "success" | "error" | "" | "info";
  }>({ message: "", type: "" });

  const modalStyle = {
    position: "absolute" as "absolute",
    top: "50%",
    left: "50%",
    padding: "0.5rem 2rem 2rem 2rem",
    transform: "translate(-50%, -50%)",
    width: "60%",
    "@media (min-width:450px)": {
      width: "50%",
    },
    "@media (min-width:600px)": {
      width: "40%",
    },
    "@media (min-width:1050px)": {
      width: "20%",
    },
    backgroundColor: palette.primary.light,
    borderRadius: "1.25rem",
  };


  const handleClosePasswordModal = () => {
    props.setPasswordModalOpen(false);
    setOldPassword("");
    setNewPassword("");
  };

  const clearAlert = () => {
    setAlertState({ message: "", type: "" });
  }

  const handleSubmit = async () => {
    setOldPassword("");
    setNewPassword("");
    const token = sessionStorage.getItem("token");
    if (!token) {
      throw new Error("Token not found");
    }
    const decoded = jwtDecode<{ userId: number }>(token!);
    const userId = decoded.userId;

    const body = {
      userId: userId,
      oldPassword: oldPassword,
      newPassword: newPassword,
    };
    

    try {
      const response = await fetch("http://localhost:8080/api/user/changePassword", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
        body: JSON.stringify(body),
      });

      const message = await response.text();
      if (response.status === 200) {
        setAlertState({
          message: "Password changed successfully",
          type: "success",
        });
      } else {
        setAlertState({ message:message, type: "error" });
      }
    } catch (err) {
      setAlertState({ message: "Password change failed", type: "error" });
    }
  };

  return (
    <Modal
      open={props.isPasswordModalOpen}
      onClose={handleClosePasswordModal}
      aria-labelledby="change-password-modal"
      aria-describedby="change-password-form"
    >
      <Box sx={modalStyle}>
        <Box sx={headerStyle("space-around")}>
          <Typography fontWeight={"bold"} fontSize={"1.25rem"}>
            Change Password
          </Typography>
          <IconButton size="medium" onClick={handleClosePasswordModal}>
            <HighlightOffOutlined
              fontSize="large"
              sx={{ color: palette.primary.main }}
            />
          </IconButton>
        </Box>
        <Box
          display={"flex"}
          justifyContent={"center"}
          alignItems={"center"}
          flexDirection={"column"}
        >
          <TextField
            margin="normal"
            type="password"
            id="Old password"
            label="Old password"
            name="Old password"
            autoFocus
            variant="outlined"
            value={oldPassword}
            onChange={(e) => setOldPassword(e.target.value)}
            InputProps={{ inputProps: { min: 0 } }}
            InputLabelProps={{
              sx: {
                "&.Mui-focused": {
                  color: palette.text.secondary,
                },
              },
            }}
            sx={{ ...inputStyle, width: "70%" }}
          />
          <TextField
            margin="normal"
            type="password"
            id="new password"
            label="New password"
            name="New password"
            autoFocus
            variant="outlined"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            InputProps={{ inputProps: { min: 0 } }}
            InputLabelProps={{
              sx: {
                "&.Mui-focused": {
                  color: palette.text.secondary,
                },
              },
            }}
            sx={{ ...inputStyle, width: "70%" }}
          />
          <Button
            sx={buttonStyle(palette.secondary.main,"50%")}
            onClick={handleSubmit}
            disabled={oldPassword.length < 9 || newPassword.length < 9}
          >
            Submit
          </Button>
          {alertState.message && <Alert sx={{marginTop:"1rem"}} severity={alertState.type || undefined} onClose={clearAlert}>{alertState.message}</Alert>}
        </Box>
      </Box>
    </Modal>
  );
};

export default ChangePasswordModal;
