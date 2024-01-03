import { headers } from "next/headers";
import Layout from "../navLayout";
import RegisterForm from "@/components/RegisterPage/RegisterForm";



export default function Register(){
    const headersList = headers();
    const activePath = headersList.get("x-invoke-path");
  
      return (
        <>
        <Layout path={activePath?activePath:"/signup"}/>
        <RegisterForm/>
        </>
      )
}