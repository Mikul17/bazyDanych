"use client";
import { IconButton, Box, Typography, Button } from "@mui/material";
import {
  PersonOutlineRounded,
  ArticleOutlined,
  HomeRounded,
  TableChartOutlined,
  CalendarMonthOutlined,
} from "@mui/icons-material";
import paletteProvider from "@/constants/color-palette";
import BalanceDropdown from "./BalanceDropdown";
import Link from "next/link";
import { useEffect, useState } from "react";
import UserDropdown from "./UserDropdown";

type NavbarProps = {
  path:string;
}


const Navbar = (props:NavbarProps) => {
  const palette = paletteProvider();
  const [currPath , setCurrPath] = useState<string>();

  useEffect(() => {
    const url = props.path==="/"?"home":props.path.slice(1);
    setCurrPath(url.charAt(0).toUpperCase() + url.slice(1)); 
  },[props.path])

  return (
    <Box
      sx={{
        position: "relative",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        background: palette.primary.light,
        width: "100%",
        height: "4.5rem",
      }}
    >
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <UserDropdown path={currPath?currPath:""}/>

        <Link href={"/coupons"}>
          <IconButton>
            <ArticleOutlined sx={{ fontSize: "2.5rem", color: currPath==="Coupons"?palette.primary.dark:palette.text.secondary }} />
          </IconButton>
        </Link>

        <BalanceDropdown balance={123.23} />
      </Box>  
      
      <Box sx={{display: "flex",
        justifyContent: "center",
        alignItmes: "center",
        background:palette.primary.main,
        padding:"0rem 4rem",
        borderRadius:"1rem"}}>
        <Typography sx={{
          fontSize:"2rem",
          fontWeight:"bold",
          color:palette.text.secondary
        }}>{currPath}</Typography>
      </Box>

      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <Link href={""}>
          <IconButton>
            <HomeRounded sx={{ fontSize: "2.5rem", color: currPath==="Home"?palette.primary.dark:palette.text.secondary }} />
          </IconButton>
        </Link>
        <Link href={"/leagues"}>
          <IconButton>
            <TableChartOutlined sx={{ fontSize: "2.5rem", color: currPath==="League"?palette.primary.dark:palette.text.secondary }} />
          </IconButton>
        </Link>
        <Link href={"/matches"}>
          <IconButton>
            <CalendarMonthOutlined
              sx={{ fontSize: "2.5rem", color: currPath==="Matches"?palette.primary.dark:palette.text.secondary }}
            />
          </IconButton>
        </Link>
      </Box>
    </Box>
  );
};

export default Navbar;
