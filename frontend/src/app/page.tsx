import Navbar from "@/components/Navbar/Navbar";
import { AppBar } from "@mui/material";


export default function Home() {
  return (
    <>
    <AppBar>
    <Navbar activePage={"home"}/>
    </AppBar>
      
    </>
  )
}
