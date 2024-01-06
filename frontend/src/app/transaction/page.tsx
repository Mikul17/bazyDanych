import { headers } from "next/headers";
import Layout from "../navLayout";
import TransactionList from "@/components/TransactionPage/TransactionList";


export default function Transaction(){
    const headersList = headers();
    const activePath = headersList.get("x-invoke-path");
  
    return (
      <>
      <Layout path={activePath?activePath:"/transaction"}/>
      <TransactionList/>
      </>
    )
}