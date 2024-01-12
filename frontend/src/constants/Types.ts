
export type Transaction = {
    id: number;
    userId: number;
    amount: number;
    transactionType: string;
    transactionDate: Date;
}

export type Address = {
    country: string;
    city: string;
    street: string;
    streetNumber: string;
    houseNumber: string;
    zipCode: string;
}

export type User = {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    birthDate: string | Date;
    phoneNumber: string;
    accountNumber: string;
    address: Address;
    isBanned?: boolean;
}

export type BetType = {
    id: number;
    betStat: string;
    betTypeCode: string;
    targetValue: number;
    team: number;
}

export type Bet = {
    id: number;
    matchId: number;
    homeTeam: string;
    awayTeam: string;
    odds: number;
    betStatus: number;
    betType: BetType;
}

export type Coupon = {
    id: number;
    couponStatus: string;
    createdAt: string;
    stake: number;
    possibleWin: number;
    totalOdds: number;
    events: Array<any>;
}

export type Match = {
    id: number;
    homeTeam: string;
    awayTeam: string;
    league: string;
    matchDate: string | Date;
}

export type MatchHistory = {
    matchId: number;
    homeTeam: string;
    homeTeamScore: number;
    awayTeam: string;
    awayTeamScore: number;
    matchDate: string | Date;
}

export type League = {
    id: number;
    leagueName: string;
    country: string;
    remainingMatches: number;
    season: number;
}

export type Team = {
    id: number;
    teamName: string;
    league: League;
    wins: number;
    draws: number;
    loses: number;
    goalsScored: number;
    goalsConceded: number;
    leaguePoints: number;
}