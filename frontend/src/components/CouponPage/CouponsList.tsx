"use client";
import { headerStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import { Box, Container, Modal, Stack, Typography } from "@mui/material";
import CouponItem from "./CouponItem";
import { jwtDecode } from "jwt-decode";
import { useEffect, useState } from "react";
import { Coupon } from "@/constants/Types";
import CouponDetailsModal from "./CouponDetailsModal";


const fetchCoupons = async (): Promise<Coupon[]> => {
    const token = sessionStorage.getItem("token");
    if(!token) throw new Error("No token");
    const decoded = jwtDecode<{ userId: number }>(token!);
    const userId = decoded.userId;
    const url = `http://localhost:8080/api/coupon/all/${userId}`;
    const requestOptions = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
    }

        try{
            const response = await fetch(url, requestOptions);
            const data:Coupon[] = await response.json();
            if(!response.ok){
                throw new Error("Error while fetching coupons");
            }else{
                return data;
            }
        } catch (error){
            console.log(error);
            return {} as Coupon[];
        }
};



const CouponsList = () => {
    const palette = paletteProvider();
    const [coupons, setCoupons] = useState<Coupon[]>([]);

    const boxStyle = {
        padding: "0.5rem 1rem 2rem 1rem",
        backgroundColor: palette.primary.light,
        borderRadius: "1.25rem",
        height: "75vh",
        overflowY: "scroll",
        "&::-webkit-scrollbar": {
           display:"none"
        },
      };

    useEffect(()=> {
        const getCoupons = async () => {
            try{
                const couponsData = await fetchCoupons();
                setCoupons(couponsData);
            }catch(error){
                console.log(error);
            }      
        };
        getCoupons();
    },[])

    return (
        <Container maxWidth="sm" sx={{ mt: "2rem" }}>
            <Box sx={boxStyle}>
            <Box sx={headerStyle("center")}>
                <Typography fontWeight={"bold"} fontSize={"2rem"}>Coupons List</Typography>
            </Box>
            <Stack spacing={2} justifyContent={"center"} alignItems={"center"}>
                {coupons.map((coupon:Coupon) => (
                    <CouponItem key={coupon.id} coupon={coupon} />
                ))}
            </Stack>
            </Box>
        </Container>    
    );
}

export default CouponsList;