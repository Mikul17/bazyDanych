import Layout from "./navLayout";
import MatchContainer from "@/components/HomePage/MatchContainer";
import {Alert, Box,Grid} from "@mui/material";
import CouponCreator from "@/components/HomePage/CouponCreator";
import { headers } from "next/headers";



export default function Home() {
 const headersList = headers();
 const activePath = headersList.get("x-invoke-path");

  return (
    <>
    <Layout path={activePath?activePath:"/"}/>
    <Grid container spacing={2}>
        <Grid item xs={12} md={7} overflow={"auto"} height={"85vh"} marginTop={"1.5rem"} sx={{'&::-webkit-scrollbar': {display: "none"}}}>
          {/* <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} />
          <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} />
          <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} />
          <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} />
          <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} /> */}
        </Grid>
        <Grid item xs={12} md={5} height={"50vh"}>
          <Box >
            <CouponCreator/>
          </Box>
        </Grid>
      </Grid>
    </>

  )
}