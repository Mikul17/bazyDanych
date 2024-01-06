"use client";
import { buttonStyle, headerStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import {
  Box,
  Button,
  Card,
  Container,
  Grid,
  SelectChangeEvent,
  Typography,
} from "@mui/material";
import { ChangeEvent, useEffect, useState } from "react";
import { Address, User } from "@/constants/Types";
import { jwtDecode } from "jwt-decode";
import PersonalDetails from "./PersonalDetails";
import AddressDetails from "./AddressDetails";
import ChangePasswordModal from "./ChangePasswordModal";
import UserListModal from "./UserListModal";

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
    return data;
  } catch (error) {
    console.error(error);
    return {} as User;
  }
};

const UserSettings = () => {
  const palette = paletteProvider();

  const [isAdmin, setIsAdmin] = useState<boolean>(false);
  const [isAdminModalOpen, setIsAdminModalOpen] = useState<boolean>(false);

  const [editPhoneNumber, setEditPhoneNumber] = useState<boolean>(false);
  const [editAccountNumber, setEditAccountNumber] = useState<boolean>(false);
  const [phoneNumberInput, setPhoneNumberInput] = useState<string>("");
  const [accountNumberInput, setAccountNumberInput] = useState<string>("");

  const [changePasswordModalOpen, setChangePasswordModalOpen] =
    useState<boolean>(false);

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

  const handleOpenPasswordModal = () => {
    setChangePasswordModalOpen(true);
  };

  const handleOpenAdminModal = () => {
    setIsAdminModalOpen(true);
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
    <>
      <Container maxWidth="lg" sx={{ mt: 4 }}>
        <Box sx={boxStyle}>
          <Box sx={headerStyle("center")}>
            <Typography variant="h4" fontWeight={"bold"}>
              {isAdmin ? "Admin settings" : "User settings"}
            </Typography>
          </Box>
          <Grid container spacing={4}>
            <PersonalDetails
              user={user}
              editPhoneNumber={editPhoneNumber}
              editAccountNumber={editAccountNumber}
              togglePhoneNumberEdit={togglePhoneNumberEdit}
              toggleAccountNumberEdit={toggleAccountNumberEdit}
              handlePhoneChange={handlePhoneChange}
              handleAccountNumberChange={handleAccountNumberChange}
              phoneNumberInput={phoneNumberInput}
              accountNumberInput={accountNumberInput}
            />

            <Grid item xs={12} md={6}>
              <Card raised sx={cardStyle}>
                <AddressDetails
                  currentAddress={currentAddress}
                  handleAddressChange={handleAddressChange}
                  handleCountryChange={handleCountryChange}
                  handleRestrictedInputChange={handleRestrictedInputChange}
                  handleSaveChanges={handleSaveChanges}
                  handleDiscardChanges={handleDiscardChanges}
                  hasChanges={hasChanges}
                />
              </Card>
              <Box
                display={"flex"}
                justifyContent={"center"}
                alignItems={"center"}
                flexDirection={"column"}
                marginTop={"0.5rem"}
              >
                <Button
                  disabled={!editPhoneNumber && !editAccountNumber}
                  onClick={handleUpdatePersonalDetails}
                  sx={buttonStyle(palette.primary.main, "60%")}
                >
                  Update personal details
                </Button>
                <Button
                  disabled={editPhoneNumber || editAccountNumber}
                  onClick={handleOpenPasswordModal}
                  sx={buttonStyle(palette.primary.main, "60%")}
                >
                  Change password
                </Button>
                {isAdmin && (
                  <Button
                    sx={buttonStyle(palette.primary.main, "60%")}
                    onClick={handleOpenAdminModal}
                  >
                    User list
                  </Button>
                )}
              </Box>
            </Grid>
          </Grid>
        </Box>
        <ChangePasswordModal
          isPasswordModalOpen={changePasswordModalOpen}
          setPasswordModalOpen={setChangePasswordModalOpen}
        />
        <UserListModal
          isAdminModalOpen={isAdminModalOpen}
          setIsAdminModalOpen={setIsAdminModalOpen}
        />
      </Container>
    </>
  );
};

export default UserSettings;
