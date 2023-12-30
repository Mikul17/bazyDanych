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
import { auto } from "@popperjs/core";
import { flexbox } from "@mui/system";

interface NavbarProps {
    activePage: string;
}

const Navbar = (props:NavbarProps) => {
    const palette = paletteProvider();

  return (
    <Box
      sx={{
        position: "fixed",
        display: "flex",
        justifyContent: "space-between",
        alignItmes: "center",
        background:palette.primary.light,
        width:"100%",
        height:"4.5rem"
      }}
    >

    <Box sx={{
        display:"flex",
        justifyContent:"center",
        alignItems:"center"
    }}>
        <IconButton>
            <PersonOutlineRounded sx={{fontSize:"2.5rem", color:"white"}}/>
        </IconButton>
        <IconButton>
            <ArticleOutlined sx={{fontSize:"2.5rem", color:"white"}}/>
        </IconButton>
        <BalanceDropdown balance={123.23} />
    </Box>

    <Box sx={{display:"flex", justifyContent:"center", alignItems:"center"}}>
    <Button disabled={true} sx={{background:palette.primary.main, padding:"0.5rem 8rem", 
    borderRadius:"0.75rem",
    "&.Mui-disabled": {
        color: palette.text.secondary
      }
    }}>
        <Typography>{props.activePage}</Typography>
    </Button>
    </Box>

    <Box sx={{
        display:"flex",
        justifyContent:"center",
        alignItems:"center"
    }}>
        <IconButton>
            <HomeRounded sx={{fontSize:"2.5rem", color:"white"}}/>
        </IconButton>
        <IconButton>
            <TableChartOutlined sx={{fontSize:"2.5rem", color:"white"}}/>
        </IconButton>
        <IconButton>
            <CalendarMonthOutlined sx={{fontSize:"2.5rem", color:"white"}}/>
        </IconButton>
    </Box>


    </Box>
  );
}

export default Navbar;
