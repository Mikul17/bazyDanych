"use client";

import paletteProvider from "@/constants/color-palette";
import {
  Alert,
  Autocomplete,
  Box,
  Button,
  Container,
  Grid,
  List,
  ListItem,
  Paper,
  SelectChangeEvent,
  TextField,
  Typography,
  alpha,
} from "@mui/material";
import { MuiTelInput } from "mui-tel-input";
import { ChangeEvent, FormEvent, useState } from "react";
import CountrySelector from "./CountrySelector";
import { buttonStyle, headerStyle, inputStyle } from "@/constants/Styles";
import { AssignmentTurnedInOutlined } from "@mui/icons-material";

interface FormState {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  country: string;
  city: string;
  street: string;
  streetNumber: string;
  houseNumber: string;
  zipCode: string;
  accountNumber: string;
  ssn: string;
  password: string;
}

const RegisterForm = () => {
  const palette = paletteProvider();
  const [alertMessage, setAlertMessage] = useState<string>("");
  const [alertType, setAlertType] = useState<"error" | "success">("error");
  const [formSubmitted, setFormSubmitted] = useState<boolean>(false);
  const [formData, setFormData] = useState<FormState>({
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    country: "",
    city: "",
    street: "",
    streetNumber: "",
    houseNumber: "",
    zipCode: "",
    accountNumber: "",
    ssn: "",
    password: "",
  });


  const paperStyle = {
    marginTop: "1rem",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: "2rem",
    paddingTop: "0.5rem",
    backgroundColor: palette.primary.light,
    borderRadius: "1.25rem",
  };
  const listItemStyle = {
    listStyleType: "disc",
    display: "list-item",
    margin: "0 1rem",
  };


  async function handleSubmit(event: FormEvent<HTMLFormElement>): Promise<void> {
    event.preventDefault();
    formData.phoneNumber = formData.phoneNumber.replace(/\D/g, '');

    try{
        const response = await fetch('http://localhost:8080/api/auth/signup',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        });
        if(response.ok){
            const message = await response.text();
            setAlertType("success");
            setAlertMessage(message || "success");
            setFormSubmitted(true);
        }else{
            const message = await response.text();
            setAlertType("error");
            setAlertMessage(message || "error");
            setFormSubmitted(true);
        }
    }catch(errorResponse){
        if (errorResponse instanceof Response) {
            const errorMessage = await errorResponse.text();
            setAlertMessage(errorMessage);
            setAlertType("error");
        }
            else{
                setAlertType("error");
                setAlertMessage("Error while connecting to server");
                setFormSubmitted(true);
            }
        }
  }

  const handleInputChange = (name: string, value: string) => {
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handlePhoneChange = (phoneValue: string) => {
    handleInputChange("phoneNumber", phoneValue);
  };

  const handleCountryChange = (event: SelectChangeEvent) => {
    handleInputChange("country", event.target.value as string);
  };

  const handleNumericChange = (event: ChangeEvent<HTMLInputElement>) => {
    let { name, value } = event.target;
    value = value.replace(/\D/g, '');
    handleInputChange(name, value);
  }

  const handleStandardChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    const charFields = ['firstName', 'lastName', 'city', 'street'];
    let newValue = value;
    

    if (charFields.includes(name)) {
      newValue = newValue.replace(/\d/g, '');
    }
  
    handleInputChange(name, newValue);
  };

  return (
    <Container component="main" maxWidth="md">
      <Paper elevation={3} sx={paperStyle}>
        <Box sx={headerStyle("center")} width={"100%"}>
          <Typography
            component="h1"
            variant="h5"
            fontWeight={"bold"}
            sx={{ color: palette.primary.main }}
          >
            Create new account
          </Typography>
        </Box>
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={4}>
              <TextField
                autoComplete="given-name"
                name="firstName"
                required
                fullWidth
                id="firstName"
                label="First Name"
                autoFocus
                value={formData.firstName}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                onChange={handleStandardChange}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <TextField
                required
                fullWidth
                id="lastName"
                label="Last Name"
                name="lastName"
                autoComplete="family-name"
                value={formData.lastName}
                onChange={handleStandardChange}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <MuiTelInput
                required
                fullWidth
                id="phone"
                label="Phone Number"
                name="phone"
                autoComplete="tel"
                value={formData.phoneNumber}
                onChange={handlePhoneChange}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <CountrySelector value={formData.country} onChange={handleCountryChange}/>
            </Grid>
            <Grid item xs={12} sm={4}>
              <TextField
                required
                fullWidth
                id="city"
                label="City"
                name="city"
                autoComplete="address-level2"
                value={formData.city}
                onChange={handleStandardChange}
                inputProps={{ maxLength: 50 }}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <TextField
                required
                fullWidth
                id="zipCode"
                label="Postal Code"
                name="zipCode"
                autoComplete="zip-code"
                value={formData.zipCode}
                onChange={handleStandardChange}
                inputProps={{ maxLength: 6 }}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <TextField
                required
                fullWidth
                id="street"
                label="Street"
                name="street"
                autoComplete="street-address"
                value={formData.street}
                onChange={handleStandardChange}
                inputProps={{ maxLength: 50 }}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <TextField
                required
                fullWidth
                id="streetNumber"
                label="Street Number"
                name="streetNumber"
                inputProps={{ maxLength: 5 }}
                value={formData.streetNumber}
                onChange={handleStandardChange}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <TextField
                fullWidth
                id="houseNumber"
                label="House Number"
                name="houseNumber"
                value={formData.houseNumber}
                onChange={handleStandardChange}
                inputProps={{ maxLength: 5 }}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={12} sm={8}>
              <TextField
                required
                fullWidth
                id="accountNumber"
                label="Account Number"
                name="accountNumber"
                inputProps={{ maxLength: 26, pattern: "\\d*" }}
                value={formData.accountNumber}
                onChange={handleNumericChange}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                sx={inputStyle}
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <TextField
                required
                fullWidth
                id="ssn"
                label="SSN"
                name="ssn"
                sx={inputStyle}
                value={formData.ssn}
                inputProps={{ maxLength: 11 }}
                InputLabelProps={{sx:{"&.Mui-focused": {
                    color: palette.text.secondary,
                  },}}}
                onChange={handleNumericChange}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Grid container spacing={2} direction="column">
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    id="email"
                    label="Email"
                    name="email"
                    autoComplete="email"
                    value={formData.email}
                    onChange={handleStandardChange}
                    InputLabelProps={{sx:{"&.Mui-focused": {
                        color: palette.text.secondary,
                      },}}}
                    sx={inputStyle}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    id="password"
                    label="Password"
                    name="password"
                    type="password"
                    autoComplete="new-password"
                    value={formData.password}
                    onChange={handleStandardChange}
                    InputLabelProps={{sx:{"&.Mui-focused": {
                        color: palette.text.secondary,
                      },}}}
                    sx={inputStyle}
                  />
                  {formSubmitted && <Alert sx={{marginTop:"1rem"}} severity={alertType}>{alertMessage}</Alert>}
                </Grid>
              </Grid>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Paper elevation={0} style={{ backgroundColor: "transparent" }}>
                <Box
                  sx={{
                    backgroundColor: palette.primary.main,
                    padding: "1rem",
                    borderRadius: "1rem",
                    color: palette.text.secondary,
                  }}
                >
                  <Typography variant="body2" sx={{ fontWeight: "bold" }}>
                    Your password needs to have:
                  </Typography>
                  <List sx={{ padding: 0 }}>
                    <ListItem sx={listItemStyle}>
                      at least 9 characters long
                    </ListItem>
                    <ListItem sx={listItemStyle}>lowercase letter</ListItem>
                    <ListItem sx={listItemStyle}>uppercase letter</ListItem>
                    <ListItem sx={listItemStyle}>number</ListItem>
                    <ListItem sx={listItemStyle}>
                      special character ('!', '@', '#', '$', '%', '?')
                    </ListItem>
                  </List>
                </Box>
              </Paper>
            </Grid>
          </Grid>
          <Button type="submit" fullWidth variant="contained" sx={buttonStyle(palette.secondary.main)} endIcon={<AssignmentTurnedInOutlined/>}>
            Sign Up
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default RegisterForm;
