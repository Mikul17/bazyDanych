import LeagueTableContainer from "@/components/LeagueTable/LeagueTableContainer";
import Layout from "../navLayout";


export default function LeagueTablePage() {
    return (
        <>
        <Layout path={"/leagues"} />
        <LeagueTableContainer />
        </>
    )
}