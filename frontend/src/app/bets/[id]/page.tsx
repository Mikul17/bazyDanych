import Layout from "@/app/navLayout";
import BetsList from "@/components/BetListPage/BetsList";
import CouponCreator from "@/components/HomePage/CouponCreator";
import TransactionList from "@/components/TransactionPage/TransactionList";
import { Grid } from "@mui/material";

export default function BetsListPage({ params }: { params: { id: string } }) {
  return (
    <>
      <Layout path={"/bets"} />
      <Grid container spacing={1}>
        <Grid item md={8} mt={2}>
      <BetsList match={{
              id: 1,
              homeTeam: "Legia Warszawa",
              awayTeam: "Śląsk Wrocław",
              leagie: "",
              matchDate: ""
          }} />
          </Grid>
          <Grid item md={4} mt={2}>
            <CouponCreator />
            </Grid>
          </Grid>
    </> 
  );
}
