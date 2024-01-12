import UserSettings from "@/components/UserPage/UserSettings";
import Layout from "../navLayout";



export default function UserPage(){

    return (
      <>
      <Layout path={"/user"}/>
      <UserSettings/>
      </>
    )
}