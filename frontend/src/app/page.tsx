"use client"
import { usePathname } from "next/navigation";
import Layout from "./navLayout";
import MatchContainer from "@/components/MatchContainer";
import { Box, Button, Card, CardContent, Grid, Typography } from "@mui/material";
import CouponCreator from "@/components/CouponCreator";



export default function Home() {
  const path : string = usePathname();

  return (
    <>
    <Layout path={path}/>
    <Grid container spacing={2}>
        <Grid item xs={12} md={7} overflow={"auto"} height={"90vh"}>
          <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} />
          <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} />
          <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} />
          <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} />
          <MatchContainer homeTeam={"Legia Warszawa"} awayTeam={"Śląsk Wrocław"} matchDate={"25 Dec 2023"} matchTime={"13:30"} />
        </Grid>
        <Grid item xs={12} md={5} height={"50vh"}>
          <Box>
            <CouponCreator/>
          </Box>
        </Grid>
      </Grid>
    </>

  )
}