"use client";

import paletteProvider from "@/constants/color-palette";
import { Box, Button, Menu, MenuItem, Modal, Typography, alpha } from "@mui/material";
import {
  bindMenu,
  bindTrigger,
  usePopupState,
} from "material-ui-popup-state/hooks";
import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { useAuth } from "@/context/AuthContext";
import NewTransactionModal from "./NewTransactionModal";

async function getBalance(): Promise<number> {
  const token = sessionStorage.getItem("token");
  if (!token) {
    throw new Error("Token not found");
  }
  try {
    const decoded = jwtDecode<{ userId: number }>(token!);
    const userId = decoded.userId;

    if (!userId) {
      throw new Error("User id not found");
    }

    const url = "http://localhost:8080/api/user/balance/" + userId;
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${token}`,
      }, });

    if (!response.ok) {
      throw new Error("Error while fetching balance");
    }

    const balance = await response.json();
    return balance;
  } catch (error) {
    console.error(error);
    return -1.00;
  }
}

const BalanceDropdown = () => {
  const palette = paletteProvider();
  const [balance, setBalance] = useState<number>(0.00);
  const {isLogged} = useAuth();
  const popupState = usePopupState({
    variant: "popover",
    popupId: "balancePopup",
  });
  const [openModal, setOpenModal] = useState(false);

  const menuItemSx = {
    display: "flex",
    allignItems: "center",
    justifyContent: "center",
    backgroundColor: palette.primary.light,
    color: palette.text.secondary,
    margin: "0.5rem 1rem",
    borderRadius: "0.5rem",
    "&:hover": {
      backgroundColor: alpha(palette.primary.light, 0.6),
    },
  };



    const handleOpenModal = () => {
      popupState.close();
      setOpenModal(true);
    };
  
    const handleCloseModal = () => {
      setOpenModal(false);
    };

    const fetchBalance = async () => {
      try {
        const newBalance = await getBalance();
        setBalance(newBalance);
      } catch (error) {
        console.error('Failed to fetch balance:', error);
      }
    };

  useEffect(() => {
    let interval: NodeJS.Timeout;

    if (isLogged) {
      fetchBalance();
      //Update balance every 5 minutes
      interval = setInterval(fetchBalance,  5*60 * 1000);
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isLogged]);

  if (!isLogged) {
    return null;
  }

  return (
    <>
      <Button
        sx={{
          background: palette.primary.main,
          color: palette.text.secondary,
          borderRadius: "1rem",
          padding: "0.5rem 2rem",
          textTransform: "none",
          "&:hover": {
            background: alpha(palette.primary.main, 0.6),
          },
        }}
        {...bindTrigger(popupState)}
      >
        <Typography>{balance} zł</Typography>
      </Button>
      <Menu
        {...bindMenu(popupState)}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
        transformOrigin={{ vertical: "top", horizontal: "left" }}
        sx={{
          ".MuiMenu-list": {
            background: palette.primary.main,
          },
          ".MuiMenu-paper": {
            borderRadius: "1rem",
          },
        }}
      >
        <MenuItem onClick={handleOpenModal} sx={menuItemSx}>
          Deposit/Withdraw
        </MenuItem>
        <MenuItem onClick={popupState.close} sx={menuItemSx}>
          Transaction list
        </MenuItem>
      </Menu>
      <NewTransactionModal openModal={openModal} handleCloseModal={handleCloseModal} updateBalance={fetchBalance}/>
    </>
  );
};

export default BalanceDropdown;
