import Layout from "@/app/navLayout";
import BetsList from "@/components/BetListPage/BetsList";
import CouponCreator from "@/components/HomePage/CouponCreator";
import TransactionList from "@/components/TransactionPage/TransactionList";
import { Grid } from "@mui/material";

export default function BetsListPage({ params }: { params: { id: string } }) {
  return (
    <>
      {/* <Layout path={"/bets"} /> */}
      <BetsList match={{
              id: 0,
              homeTeam: "Legia Warszawa",
              awayTeam: "Śląsk Wrocław",
              leagie: "",
              matchDate: ""
          }} />
    </> 
  );
}
