"use client";
import { buttonStyle, headerStyle, inputStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import {
  Box,
  Button,
  Card,
  CardContent,
  Container,
  Grid,
  IconButton,
  SelectChangeEvent,
  TextField,
  Typography,
} from "@mui/material";
import { ChangeEvent, useEffect, useState } from "react";
import CountrySelector from "../RegisterPage/CountrySelector";
import { ClearOutlined, SettingsOutlined } from "@mui/icons-material";
import { MuiTelInput } from "mui-tel-input";
import { Address, User } from "@/constants/Types";
import { jwtDecode } from "jwt-decode";

const getUser = async (): Promise<User> => {
  const token = sessionStorage.getItem("token");
  if (!token) {
    throw new Error("No token found");
  }
  const decoded = jwtDecode<{ userId: number }>(token!);
  const userId = decoded.userId;
  const url = "http://localhost:8080/api/user/" + userId;

  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Error while fetching user data");
    }

    const data: User = await response.json();
    console.log(data);
    return data;
  } catch (error) {
    console.error(error);
    return {} as User;
  }
};

const UserSettings = () => {
  const palette = paletteProvider();

  const [isAdmin, setIsAdmin] = useState<boolean>(false);

  const [editPhoneNumber, setEditPhoneNumber] = useState<boolean>(false);
  const [editAccountNumber, setEditAccountNumber] = useState<boolean>(false);
  const [phoneNumberInput, setPhoneNumberInput] = useState<string>("");
  const [accountNumberInput, setAccountNumberInput] = useState<string>("");

  const [hasChanges, setHasChanges] = useState<boolean>(false);
  const [initialAddress, setInitialAddress] = useState<Address | null>(null);
  const [currentAddress, setCurrentAddress] = useState<Address>({
    country: "",
    city: "",
    street: "",
    houseNumber: "",
    streetNumber: "",
    zipCode: "",
  });

  const [user, setUser] = useState<User>({
    id: 0,
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    accountNumber: "",
    birthDate: "",
    address: currentAddress,
  });

  const boxStyle = {
    padding: "0.5rem 1rem 2rem 1rem",
    backgroundColor: palette.primary.light,
    borderRadius: "1.25rem",
  };

  const cardStyle = {
    backgroundColor: palette.primary.main,
    borderRadius: "1.25rem",
    padding: "1rem ",
  };

  const innerHeaderStyle = {
    ...headerStyle("center"),
    margin: 0,
  };

  const togglePhoneNumberEdit = () => {
    if (editPhoneNumber) {
      setPhoneNumberInput(user.phoneNumber);
      setEditPhoneNumber(false);
    } else {
      setPhoneNumberInput("");
      setEditPhoneNumber(true);
    }
  };

  const toggleAccountNumberEdit = () => {
    if (editAccountNumber) {
      setAccountNumberInput(user.accountNumber);
      setEditAccountNumber(false);
    } else {
      setAccountNumberInput("");
      setEditAccountNumber(true);
    }
  };

  const handlePhoneChange = (phoneValue: string) => {
    setPhoneNumberInput(phoneValue);
  };

  const handleAccountNumberChange = (event: ChangeEvent<HTMLInputElement>) => {
    let value = event.target.value;
    value = value.replace(/\D/g, "");
    setAccountNumberInput(value);
  };

  const handleAddressChange = (field: keyof Address, value: string) => {
    setCurrentAddress((prev) => ({ ...prev, [field]: value }));
    setHasChanges(true);
  };

  const handleRestrictedInputChange = (
    field: "zipCode" | "houseNumber" | "streetNumber",
    event: ChangeEvent<HTMLInputElement>
  ) => {
    let pattern = /^[0-9a-zA-Z-]*$/;

    if (pattern.test(event.target.value)) {
      handleAddressChange(field, event.target.value);
    }
  };

  const handleCountryChange = (event: SelectChangeEvent) => {
    setCurrentAddress((prev) => ({ ...prev, country: event.target.value }));
    setHasChanges(true);
  };

  const handleDiscardChanges = () => {
    if (initialAddress) {
      setCurrentAddress(initialAddress);
      setHasChanges(false);
    }
  };

  const handleSaveChanges = () => {
    const token = sessionStorage.getItem("token");
    if (!token) {
      throw new Error("No token found");
    }

    const url = "http://localhost:8080/api/user/changeAddress";
    const decoded = jwtDecode<{ userId: number }>(token!);
    const userId = decoded.userId;

    try {
      fetch(url, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          userId: userId,
          address: currentAddress,
        }),
      }).then((response) => {
        if (!response.ok) {
          throw new Error("Error while updating address");
        }
        setInitialAddress(currentAddress);
        setHasChanges(false);
      });
    } catch (error) {
      console.error(error);
    }
  };

  const updateAccountNumber = (acc: string) => {
    const token = sessionStorage.getItem("token");
    if (!token) {
      throw new Error("No token found");
    }
    const decoded = jwtDecode<{ userId: number }>(token!);
    const userId = decoded.userId;

    const queryParams = new URLSearchParams({
      accountNumber: acc,
      userId: userId.toString(),
    });

    const url =
      "http://localhost:8080/api/user/update/an/?" + queryParams.toString();

    try {
      fetch(url, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }).then((response) => {
        if (!response.ok) {
          throw new Error("Error while updating account number");
        }
      });
    } catch (error) {
      console.error(error);
    }
  };

  const updatePhoneNumber = (phone: string) => {
    const token = sessionStorage.getItem("token");
    if (!token) {
      throw new Error("No token found");
    }
    const decoded = jwtDecode<{ userId: number }>(token!);
    const userId = decoded.userId;

    const queryParams = new URLSearchParams({
      phoneNumber: phone,
      userId: userId.toString(),
    });

    const url =
      "http://localhost:8080/api/user/update/pn/?" + queryParams.toString();

    try {
      fetch(url, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }).then((response) => {
        if (!response.ok) {
          throw new Error("Error while updating phone number");
        }
      });
    } catch (error) {
      console.error(error);
    }
  };

  const handleUpdatePersonalDetails = () => {
    if (editPhoneNumber) {
      updatePhoneNumber(phoneNumberInput);
      setEditPhoneNumber(false);
    }
    if (editAccountNumber) {
      updateAccountNumber(accountNumberInput);
      setEditAccountNumber(false);
    }
    location.reload();
  };

  useEffect(() => {
    if (initialAddress) {
      const isChanged = Object.keys(initialAddress).some(
        (key) =>
          initialAddress[key as keyof Address] !==
          currentAddress[key as keyof Address]
      );
      setHasChanges(isChanged);
    }
  }, [currentAddress, initialAddress]);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const userData = await getUser();
        setUser(userData);
        setInitialAddress(userData.address);
        setCurrentAddress(userData.address);
      } catch (error) {
        console.error("Failed to fetch user: ", error);
      }
    };

    const fetchUserRole = async () => {
      const token = sessionStorage.getItem("token");
      if (!token) {
        throw new Error("No token found");
      }
      const decoded = jwtDecode<{ userId: number }>(token!);
      const userId = decoded.userId;
      try {
        const url = "http://localhost:8080/api/auth/role/" + userId;
        const response = await fetch(url, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) {
          throw new Error("Error while fetching user role");
        } else {
          const role = await response.text();
          setIsAdmin(role === "ROLE_ADMIN");
        }
      } catch (error) {
        console.error(error);
      }
    };
    fetchUser();
    fetchUserRole();
  }, []);

  return (
    <Container maxWidth="lg" sx={{ mt: 4 }}>
      <Box sx={boxStyle}>
        <Box sx={headerStyle("center")}>
          <Typography variant="h4" fontWeight={"bold"}>
            User settings
          </Typography>
        </Box>
        <Grid container spacing={4}>
          {/* Personal Details */}
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
                  value={user.firstName}
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
                  value={user.lastName}
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
                  value={user.birthDate}
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
                  value={user.email}
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
                      value={
                        editPhoneNumber ? phoneNumberInput : user.phoneNumber
                      }
                      InputLabelProps={{
                        sx: {
                          "&.Mui-focused": {
                            color: palette.text.secondary,
                          },
                        },
                      }}
                      margin="normal"
                      disabled={!editPhoneNumber}
                      onChange={handlePhoneChange}
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
                      onClick={() => togglePhoneNumberEdit()}
                    >
                      {!editPhoneNumber ? (
                        <SettingsOutlined
                          sx={{ color: palette.text.primary }}
                        />
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
                      disabled={!editAccountNumber}
                      value={
                        editAccountNumber
                          ? accountNumberInput
                          : user.accountNumber
                      }
                      sx={inputStyle}
                      inputProps={{
                        minLength: 26,
                        maxLength: 26,
                        pattern: "\\d*",
                      }}
                      InputProps={{ readOnly: !editAccountNumber }}
                      InputLabelProps={{
                        sx: {
                          "&.Mui-focused": {
                            color: palette.text.secondary,
                          },
                        },
                      }}
                      onChange={handleAccountNumberChange}
                    />
                  </Grid>
                  <Grid item xs={2}>
                    <IconButton
                      size="large"
                      sx={{
                        mt: "1.25rem",
                        backgroundColor: palette.text.secondary,
                      }}
                      onClick={() => toggleAccountNumberEdit()}
                    >
                      {!editAccountNumber ? (
                        <SettingsOutlined
                          sx={{ color: palette.text.primary }}
                        />
                      ) : (
                        <ClearOutlined sx={{ color: palette.text.primary }} />
                      )}
                    </IconButton>
                  </Grid>
                </Grid>
              </CardContent>
            </Card>
          </Grid>

          {/* Address */}
          <Grid item xs={12} md={6}>
            <Card raised sx={cardStyle}>
              <CardContent sx={{ padding: "0 1rem" }}>
                <Box sx={innerHeaderStyle}>
                  <Typography variant="h5" fontWeight={"bold"}>
                    Address
                  </Typography>
                </Box>
                <Grid container spacing={2}>
                  <Grid item xs={4}>
                    <CountrySelector
                      margin="1rem 0"
                      onChange={handleCountryChange}
                      value={currentAddress.country}
                    />
                  </Grid>
                  <Grid item xs={8}>
                    <TextField
                      label="City"
                      variant="outlined"
                      margin="normal"
                      fullWidth
                      value={currentAddress.city}
                      onChange={(e) =>
                        handleAddressChange("city", e.target.value)
                      }
                      sx={inputStyle}
                      inputProps={{ maxLength: 50, pattern: "[a-zA-Z ]*" }}
                      InputLabelProps={{
                        sx: {
                          "&.Mui-focused": {
                            color: palette.text.secondary,
                          },
                        },
                      }}
                    />
                  </Grid>
                </Grid>
                <TextField
                  label="Street"
                  variant="outlined"
                  margin="normal"
                  fullWidth
                  value={currentAddress.street}
                  onChange={(e) =>
                    handleAddressChange("street", e.target.value)
                  }
                  sx={inputStyle}
                  inputProps={{ maxLength: 50 }}
                  InputLabelProps={{
                    sx: {
                      "&.Mui-focused": {
                        color: palette.text.secondary,
                      },
                    },
                  }}
                />
                <Grid container spacing={2}>
                  <Grid item xs={4}>
                    <TextField
                      label="Street number"
                      variant="outlined"
                      margin="normal"
                      fullWidth
                      value={currentAddress.streetNumber}
                      onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                        handleRestrictedInputChange("streetNumber", e)
                      }
                      sx={inputStyle}
                      inputProps={{ maxLength: 5 }}
                      InputLabelProps={{
                        sx: {
                          "&.Mui-focused": {
                            color: palette.text.secondary,
                          },
                        },
                      }}
                    />
                  </Grid>
                  <Grid item xs={4}>
                    <TextField
                      label="House number"
                      variant="outlined"
                      margin="normal"
                      fullWidth
                      value={currentAddress.houseNumber}
                      onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                        handleRestrictedInputChange("houseNumber", e)
                      }
                      sx={inputStyle}
                      inputProps={{ maxLength: 5 }}
                      InputLabelProps={{
                        sx: {
                          "&.Mui-focused": {
                            color: palette.text.secondary,
                          },
                        },
                      }}
                    />
                  </Grid>
                  <Grid item xs={4}>
                    <TextField
                      label="Zip-code"
                      variant="outlined"
                      margin="normal"
                      fullWidth
                      value={currentAddress.zipCode}
                      onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                        handleRestrictedInputChange("zipCode", e)
                      }
                      sx={inputStyle}
                      inputProps={{ maxLength: 6, pattern: "[0-9a-zA-Z\\-/]*" }}
                      InputLabelProps={{
                        sx: {
                          "&.Mui-focused": {
                            color: palette.text.secondary,
                          },
                        },
                      }}
                    />
                  </Grid>
                </Grid>
                <Box
                  display={"flex"}
                  justifyContent={"center"}
                  alignItems={"center"}
                  marginTop={"1rem"}
                >
                  <Button
                    variant="contained"
                    disabled={!hasChanges}
                    onClick={handleSaveChanges}
                    sx={{
                      ...buttonStyle(palette.secondary.main, "60%"),
                      marginRight: "2rem",
                    }}
                  >
                    Update address
                  </Button>
                  <Button
                    onClick={handleDiscardChanges}
                    disabled={!hasChanges}
                    variant="contained"
                    color="secondary"
                    sx={buttonStyle(palette.error.main, "50%")}
                  >
                    Discard
                  </Button>
                </Box>
              </CardContent>
            </Card>
            <Box
              display={"flex"}
              justifyContent={"center"}
              alignItems={"center"}
              flexDirection={"column"}
              marginTop={"2rem"}
            >
              <Button
                disabled={!editPhoneNumber && !editAccountNumber}
                onClick={handleUpdatePersonalDetails}
                sx={buttonStyle(palette.primary.main, "60%")}
              >
                Update personal details
              </Button>
              {isAdmin && (
                <Button sx={buttonStyle(palette.primary.main, "60%")}>
                  User list
                </Button>
              )}
            </Box>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
};

export default UserSettings;
