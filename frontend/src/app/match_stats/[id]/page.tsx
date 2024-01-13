import Layout from "@/app/navLayout";
import MatchEventsContainer from "@/components/MatchStats/MatchEventsContainer";
import MatchStatsContainer from "@/components/MatchStats/MatchStatsContainer";
import TeamsSection from "@/components/MatchStats/TeamsSection";
import { Box, Grid } from "@mui/material";

export default function MatchStatsPage({ params }: { params: { id: string } }) {
  return (
    <>
      <Layout path={"/match stats"} />
      <Grid container maxHeight={"100vh"} spacing={3}>
        <Grid
          item
          xs={5}
          ml={"2rem"}
          mt={"1rem"}
          display={"flex"}
          justifyContent={"center"}
          alignItems={"center"}
          flexDirection={"column"}
        >
          <MatchEventsContainer />
          <MatchStatsContainer matchId={Number(params.id)} />
        </Grid>
        <Grid
          item
          xs={6}
          ml={"2rem"}
          mt={"1rem"}
          display={"flex"}
          justifyContent={"flex-start"}
          alignItems={"center"}
          flexDirection={"column"}
        >
          <TeamsSection matchId={Number(params.id)} />
        </Grid>
      </Grid>
    </>
  );
}
