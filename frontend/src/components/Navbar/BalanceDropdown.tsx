"use client"

import paletteProvider from "@/constants/color-palette";
import { Button, Menu, MenuItem, Typography, alpha } from "@mui/material";
import { bindMenu, bindTrigger, usePopupState } from "material-ui-popup-state/hooks";


interface BalanceProps {
    balance: number;
}



const BalanceDropdown = (props: BalanceProps) => { 
  const palette = paletteProvider();
  const popupState = usePopupState({
      variant: 'popover',
      popupId: 'balancePopup'
  });

  const menuItemSx = {
    display:"flex",
    allignItems:"center",
    justifyContent:"center",
    backgroundColor:palette.primary.light,
    color: palette.text.secondary,
    margin:"0.5rem 1rem",
    borderRadius:"0.5rem",
    "&:hover": {
      backgroundColor: alpha(palette.primary.light,0.6)
    },
  };
  return (
      <>
          <Button sx={{background:palette.primary.main, color:palette.text.secondary, borderRadius:"1rem", padding:"0.5rem 2rem", textTransform:"none"}} {...bindTrigger(popupState)}>
              <Typography>{props.balance} zł</Typography>
          </Button>
          <Menu
        {...bindMenu(popupState)}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
        transformOrigin={{ vertical: "top", horizontal: "left" }}
        sx={{
            
            ".MuiMenu-list":{
            background:palette.primary.main,
          },
          ".MuiMenu-paper":{
            borderRadius:"1rem"
          }
        }}
      >
              <MenuItem onClick={popupState.close} sx={menuItemSx}>Deposit/Withdraw</MenuItem>
              <MenuItem onClick={popupState.close} sx={menuItemSx}>Transaction list</MenuItem>
          </Menu>
      </>
  );
}

export default BalanceDropdown; 