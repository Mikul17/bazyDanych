import Layout from "./navLayout";
import {Box,Grid} from "@mui/material";
import CouponCreator from "@/components/HomePage/CouponCreator";
import { headers } from "next/headers";
import MatchListContainer from "@/components/HomePage/MatchListContainer";




export default function Home() {
 const headersList = headers();
 const activePath = headersList.get("x-invoke-path");

  return (
    <>
    <Layout path={activePath?activePath:"/"}/>
    <Grid container spacing={2}>
        <Grid item  md={8} >
          <MatchListContainer/>
        </Grid>
        <Grid item  md={4}>
          <Box >
            <CouponCreator/>
          </Box>
        </Grid>
      </Grid>
    </>

  )
}