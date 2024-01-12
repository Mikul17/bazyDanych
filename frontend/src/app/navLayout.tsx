import Navbar from "@/components/Navbar/Navbar";
import { Box } from "@mui/material";
import React, { PropsWithChildren, useEffect } from "react";


type LayoutProps = PropsWithChildren<{
    path: string; 
  }>;

const Layout = ({ children, path }: LayoutProps) => {

  return (
    <Box>
      <Navbar path={path}/>
      {children}
    </Box>
  );
};
export default Layout;