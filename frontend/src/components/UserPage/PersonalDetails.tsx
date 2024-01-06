"use client";
import { headerStyle, inputStyle } from "@/constants/Styles";
import { User } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { ClearOutlined, SettingsOutlined } from "@mui/icons-material";
import {
  Box,
  Card,
  CardContent,
  Grid,
  IconButton,
  TextField,
  Typography,
} from "@mui/material";
import { MuiTelInput } from "mui-tel-input";

interface PersonalDetailsProps {
    user: User;
    editPhoneNumber: boolean;
    editAccountNumber: boolean;
    togglePhoneNumberEdit: () => void;
    toggleAccountNumberEdit: () => void;
    handlePhoneChange: (event: any) => void;
    handleAccountNumberChange: (event: any) => void;
    phoneNumberInput: string;
    accountNumberInput: string;
    }

const PersonalDetails = (props:PersonalDetailsProps) => {
  const palette = paletteProvider();



  const cardStyle = {
    backgroundColor: palette.primary.main,
    borderRadius: "1.25rem",
    padding: "1rem ",
  };

  const innerHeaderStyle = {
    ...headerStyle("center"),
    margin: 0,
  };

  return (
    <Grid item xs={12} md={6}>
      <Card raised sx={cardStyle}>
        <CardContent sx={{ padding: "0 1rem" }}>
          <Box sx={innerHeaderStyle}>
            <Typography variant="h5" fontWeight={"bold"}>
              Personal details
            </Typography>
          </Box>
          <TextField
            label="First name"
            variant="outlined"
            margin="normal"
            fullWidth
            sx={inputStyle}
            value={props.user.firstName}
            InputProps={{ readOnly: true }}
            InputLabelProps={{
              sx: {
                "&.Mui-focused": {
                  color: palette.text.secondary,
                },
              },
            }}
          />
          <TextField
            label="Last name"
            variant="outlined"
            margin="normal"
            fullWidth
            sx={inputStyle}
            value={props.user.lastName}
            InputProps={{ readOnly: true }}
            InputLabelProps={{
              sx: {
                "&.Mui-focused": {
                  color: palette.text.secondary,
                },
              },
            }}
          />
          <TextField
            label="Date of birth"
            variant="outlined"
            margin="normal"
            fullWidth
            value={props.user.birthDate}
            sx={inputStyle}
            InputProps={{ readOnly: true }}
            InputLabelProps={{
              sx: {
                "&.Mui-focused": {
                  color: palette.text.secondary,
                },
              },
            }}
          />
          <TextField
            label="E-mail"
            variant="outlined"
            margin="normal"
            fullWidth
            value={props.user.email}
            sx={inputStyle}
            InputProps={{ readOnly: true }}
            InputLabelProps={{
              sx: {
                "&.Mui-focused": {
                  color: palette.text.secondary,
                },
              },
            }}
          />
          <Grid container spacing={2}>
            <Grid item xs={10}>
              <MuiTelInput
                required
                fullWidth
                id="phone"
                label="Phone Number"
                name="phone"
                autoComplete="tel"
                defaultCountry={"PL"}
                value={props.editPhoneNumber ? props.phoneNumberInput : props.user.phoneNumber}
                InputLabelProps={{
                  sx: {
                    "&.Mui-focused": {
                      color: palette.text.secondary,
                    },
                  },
                }}
                margin="normal"
                disabled={!props.editPhoneNumber}
                onChange={props.handlePhoneChange}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={2}>
              <IconButton
                size="large"
                sx={{
                  mt: "1.25rem",
                  backgroundColor: palette.text.secondary,
                }}
                onClick={() => props.togglePhoneNumberEdit()}
              >
                {!props.editPhoneNumber ? (
                  <SettingsOutlined sx={{ color: palette.text.primary }} />
                ) : (
                  <ClearOutlined sx={{ color: palette.text.primary }} />
                )}
              </IconButton>
            </Grid>
          </Grid>
          <Grid container spacing={2}>
            <Grid item xs={10}>
              <TextField
                label="Account number"
                variant="outlined"
                margin="normal"
                fullWidth
                disabled={!props.editAccountNumber}
                value={
                  props.editAccountNumber ? props.accountNumberInput : props.user.accountNumber
                }
                sx={inputStyle}
                inputProps={{
                  minLength: 26,
                  maxLength: 26,
                  pattern: "\\d*",
                }}
                InputProps={{ readOnly: !props.editAccountNumber }}
                InputLabelProps={{
                  sx: {
                    "&.Mui-focused": {
                      color: palette.text.secondary,
                    },
                  },
                }}
                onChange={props.handleAccountNumberChange}
              />
            </Grid>
            <Grid item xs={2}>
              <IconButton
                size="large"
                sx={{
                  mt: "1.25rem",
                  backgroundColor: palette.text.secondary,
                }}
                onClick={() => props.toggleAccountNumberEdit()}
              >
                {!props.editAccountNumber ? (
                  <SettingsOutlined sx={{ color: palette.text.primary }} />
                ) : (
                  <ClearOutlined sx={{ color: palette.text.primary }} />
                )}
              </IconButton>
            </Grid>
          </Grid>
        </CardContent>
      </Card>
    </Grid>
  );
};

export default PersonalDetails;
