"use client";

import paletteProvider from "@/constants/color-palette";
import { Box, Button, Menu, MenuItem, Modal, Typography, alpha } from "@mui/material";
import {
  bindMenu,
  bindTrigger,
  usePopupState,
} from "material-ui-popup-state/hooks";
import { useEffect, useState } from "react";
import { useAuth } from "@/context/AuthContext";
import NewTransactionModal from "./NewTransactionModal";
import { useRouter } from "next/navigation";
import { useBalance } from "@/context/BalanceContext";



const BalanceDropdown = () => {
  const palette = paletteProvider();
  const {isLogged} = useAuth();
  const { balance, fetchBalance } = useBalance();
  const router = useRouter();
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

    const handleOpenTransactionList = () => {
      popupState.close();
      router.push("/transaction");
    }
    
  
    const handleCloseModal = () => {
      setOpenModal(false);
    };

  useEffect(() => {
    let interval: NodeJS.Timeout;
    if (isLogged) {
      fetchBalance();
      //Update balance every 3 minutes
      interval = setInterval(fetchBalance,  3*60 * 1000);
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
        <MenuItem onClick={handleOpenTransactionList} sx={menuItemSx}>
          Transaction list
        </MenuItem>
      </Menu>
      <NewTransactionModal openModal={openModal} handleCloseModal={handleCloseModal} updateBalance={fetchBalance}/>
    </>
  );
};

export default BalanceDropdown;
