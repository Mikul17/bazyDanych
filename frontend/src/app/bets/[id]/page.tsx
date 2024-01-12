import Layout from "@/app/navLayout";
import BetsList from "@/components/BetListPage/BetsList";
import CouponCreator from "@/components/HomePage/CouponCreator";
import { Grid } from "@mui/material";

export default function BetsListPage({ params }: { params: { id: string } }) {

  return (
    <>
      <Layout path={"/bets"} />
      <Grid container spacing={1}>
        <Grid item md={8} mt={2}>
      <BetsList matchId={Number(params.id)}/>
          </Grid>
          <Grid item md={4} mt={2}>
            <CouponCreator />
            </Grid>
          </Grid> ``
    </> 
  );
}
