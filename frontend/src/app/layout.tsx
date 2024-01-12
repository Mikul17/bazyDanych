import { Dosis} from 'next/font/google'
import './globals.css'
import { Metadata } from 'next';
import Providers from '@/context/Providers';


const dosisFont = Dosis({
  weight: ["200", "300", "400", "500", "600", "700", "800"],
  style: ["normal"],
  subsets: ['latin'],
  display:'swap'
});

export const metadata: Metadata = {
  title: 'Betting app',
  icons:{
    icon: "icon.png",
  },
  description:
    'Betting application for database course project.',
};

export default function RootLayout({children,}: {children: React.ReactNode}) {
  return (
    <html lang="en">
      <Providers>
      <body className={dosisFont.className} style={{margin:"0", padding:"0", top:"0"}}>
          {children}
       </body>
       </Providers>
    </html>
  )
}
