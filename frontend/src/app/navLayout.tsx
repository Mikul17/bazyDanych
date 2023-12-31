import Navbar from "@/components/Navbar/Navbar";
import { Box } from "@mui/material";
import React, { PropsWithChildren } from "react";


type LayoutProps = PropsWithChildren<{
    path: string; 
  }>;

const Layout = ({ children, path }: LayoutProps) => {
  return (
    <Box sx={{position:"fixed", margin:"0", padding:"0", top:"0", left:"0"}}>
      <Navbar path={path}/>
      {children}
    </Box>
  );
};
export default Layout;