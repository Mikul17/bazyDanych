"use client";

import paletteProvider from "@/constants/color-palette";
import { PersonOutlineRounded } from "@mui/icons-material";
import { IconButton, Menu, MenuItem, Typography, alpha } from "@mui/material";
import { jwtDecode } from "jwt-decode";
import {
  bindMenu,
  bindTrigger,
  usePopupState,
} from "material-ui-popup-state/hooks";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

type UserProps = {
  path: string;
};

const UserDropdown = (props: UserProps) => {
  const palette = paletteProvider();
  const popupState = usePopupState({
    variant: "popover",
    popupId: "userPopup",
  });
  const [isLoggedIn, setIsLoggedIn] = useState<Boolean>(false);
  const router = useRouter();

  useEffect(() => {
    const token = sessionStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        if (decoded && decoded.exp && decoded.exp < Date.now() / 1000) {
          setIsLoggedIn(false);
        } else {
          setIsLoggedIn(true);
        }
      } catch (error) {
        setIsLoggedIn(false);
      }
    }
  }, []);

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

  const handleLogout = () => {
    popupState.close();
    sessionStorage.removeItem("token");
    setIsLoggedIn(false);
    router.refresh();
  };

  return (
    <>
      <IconButton {...bindTrigger(popupState)}>
        <PersonOutlineRounded
          sx={{
            fontSize: "2.5rem",
            color:
            props.path === "User" || props.path === "Login"
                ? palette.primary.dark
                : palette.text.secondary,
          }}
        />
      </IconButton>

      <Menu
        {...bindMenu(popupState)}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
        transformOrigin={{ vertical: "top", horizontal: "left" }}
        sx={{
            
            ".MuiMenu-list":{
            background:palette.primary.main,
          },
          ".MuiMenu-paper":{ 
            minWidth:"8rem",
            borderRadius:"1rem"
          }
        }}
      >
        <Link
          href={isLoggedIn ? "/user" : "/login"}
          passHref
          style={{ textDecoration: "none", color: "inherit" }}
        >
          <MenuItem onClick={popupState.close} sx={menuItemSx}>
            {isLoggedIn ? "Settings" : "Login"}
          </MenuItem>
        </Link>
        <MenuItem onClick={handleLogout} sx={menuItemSx}>
          <Typography>Logout</Typography>
        </MenuItem>
      </Menu>
    </>
  );
};

export default UserDropdown;
