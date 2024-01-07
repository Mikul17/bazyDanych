import CouponsList from "@/components/CouponPage/CouponsList";
import Layout from "../navLayout";



export default function CouponPage(){
    return (
        <>
        <Layout path={"/coupons"} />
        <CouponsList />
        </>
    )
}