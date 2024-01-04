"use client";

import paletteProvider from "@/constants/color-palette";
import { useAuth } from "@/context/AuthContext";
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

type UserProps = {
  path: string;
};

const UserDropdown = (props: UserProps) => {
  const palette = paletteProvider();
  const popupState = usePopupState({
    variant: "popover",
    popupId: "userPopup",
  });
  const { isLogged, logOut } = useAuth();
  const router = useRouter();

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
    logOut();
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
          href={isLogged ? "/user" : "/login"}
          passHref
          style={{ textDecoration: "none", color: "inherit" }}
        >
          <MenuItem onClick={popupState.close} sx={menuItemSx}>
            {isLogged ? "Settings" : "Login"}
          </MenuItem>
        </Link>
        <MenuItem onClick={handleLogout} disabled={isLogged?false:true} sx={menuItemSx}>
          <Typography>Logout</Typography>
        </MenuItem>
      </Menu>
    </>
  );
};

export default UserDropdown;
