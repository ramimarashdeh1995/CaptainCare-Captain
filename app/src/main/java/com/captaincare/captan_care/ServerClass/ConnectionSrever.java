package com.captaincare.captan_care.ServerClass;

public class ConnectionSrever {
    private static final String ROOT="http://captain-care.org/";
    private static final String NAME_URL=ROOT+"ccapp/";

    // Contact URL

    public static final String Contract=NAME_URL+"Contract/GetContract.php";

    //TODO: API TO CONTROL CAPTAIN :

    private static final String CONTROL_CAPTAIN=NAME_URL+"control_captain/";
    public static final String LogIn_Captain=CONTROL_CAPTAIN+"logIn.php";
    public static final String UpdatePassword=CONTROL_CAPTAIN+"updatePassword.php";
    public static final String SignUp_Captain=CONTROL_CAPTAIN+"signUp.php";
    public static final String UpdateProfileCaptain=CONTROL_CAPTAIN+"UpdateParsonalInformation.php";
    public static final String UbdateNewPassword=CONTROL_CAPTAIN+"UpditNewPassword.php";
    public static final String State=CONTROL_CAPTAIN+"State.php";
    //TODO: API TO CONTROL MARKET :

    private static final String CONTROL_MARKET=NAME_URL+"control_market/";
    public static final String SignUp_Market=CONTROL_MARKET+"signUp.php";
    public static final String LogInMarket=CONTROL_MARKET+"logIn.php";
    public static final String UpdatePasswordMarket=CONTROL_MARKET+"updatePassword.php";
    public static final String MyLocation=CONTROL_MARKET+"MyLocation.php";
    public static final String UpdateProfileMarket=CONTROL_MARKET+"UpdatePersonalInformation.php";

    //
    private static final String Service=NAME_URL+"Service/";
    public static final String getService=Service+"SelectAllCategory.php";
    public static final String SelectAllServiceMarket=Service+"SelectAllServiceMarket.php";
    public static final String InsertService=Service+"InsertService.php";
    public static final String SelectAllSubCategory=Service+"SelectAllSubCategory.php";
    public static final String DeleteService=Service+"DeleteService.php";

    // Favorite Offer
    private static final String Favorite=NAME_URL+"FavoriteOffer/";

    // IS Captain ......................
    private static final String FavoriteCaptain=Favorite+"FavoriteOfferCaptain/";
    public static final String MyFavoriteCaptin=FavoriteCaptain+"MyFavorite.php";
    public static final String AddFavoriteOffer=FavoriteCaptain+"AddFavoriteOffer.php";
    public static final String UnFavoriteOffer=FavoriteCaptain+"UnFavoriteOffer.php";
    public static final String SaveOfferLastGo=FavoriteCaptain+"SaveOfferLastGo.php";
    public static final String EndSaveProcessFromCaptain=FavoriteCaptain+"EndSaveProcessFromCaptain.php";

    //IS Vendor .......................
    private static final String FavoriteVendor=Favorite+"FavoriteOfferMarket/";
    public static final String MyFavoriteVendor=FavoriteVendor+"MyFavorite.php";
    public static final String AddFavoriteOfferMarket=FavoriteVendor+"AddFavoriteOffer.php";
    public static final String UnFavoriteMarket=FavoriteVendor+"UnFavorite.php";



    //Offer Captain
    private static final String Offer=NAME_URL+"OfferCaptain/";
    public static final String AddOffer=Offer+"InsertOfferCaptain.php";
    public static final String CaptainMyAds=Offer+"SelectMyOffer.php";
    public static final String DeleteOffer=Offer+"DeleteOfferCaptain.php";
    public static final String finishoffer=Offer+"endOfferCaptain.php";
    public static final String UpdateOffer=Offer+"UpdateOfferCaptain.php";
    public static final String CaptainOffer=Offer+"SelectCaptainOffer.php";
    public static final String AddPushOffer_Captain=Offer+"AddPushOffer.php";
    public static final String SelectDetailsOffer=Offer+"SelectOfferCaptain.php";

    //Offer Market
    private static final String Offer_Market=NAME_URL+"OfferMarket/";
    public static final String AddOfferMarket=Offer_Market+"InsertOfferMarket.php";
    public static final String MarketMyAds=Offer_Market+"SelectMyOffer.php";
    public static final String DeleteOfferMarket=Offer_Market+"DeleteOfferMarket.php";
    public static final String finishOfferMarket=Offer_Market+"endOfferMarket.php";
    public static final String UpdateOfferMarket=Offer_Market+ "UpdateOfferMarket.php";
    public static final String MarketOffer =Offer_Market+ "SelectMarketOffer.php";
    public static final String AddPuchOffer =Offer_Market+ "AddPuchOffer.php";


    //
    private static final String PersonalInformation=NAME_URL+"PersonalInformation/";

    private static final String Captain=PersonalInformation+"Captain/";
    private static final String Market=PersonalInformation+"Market/";

    public static  final String SelectInformaionFromCapToVen=Captain+"SelectInformaionFromCapToVen.php";
    public static  final String SelectCounterFollow=Captain+"SelectCounterFollow.php";
    public static  final String SelectInformaionCaptain=Captain+"SelectInformaionCaptain.php";
    public static  final String UpdateAboutMe=Captain+"UpdateAboutMe.php";

    public static final String SelectInformationFromVenToCap=Market+"SelectInformationFromVenToCap.php";
    public static final String SelectCounterFollowMarket=Market+"SelectCounterFollow.php";
    public static final String SelectInformationMarket=Market+"SelectInformationMarket.php";
    public static  final String UpdateAboutMeMarket=Market+"UpdateAboutMe.php";

    //
    private static final String followAndblock=NAME_URL+"followAndblock/";

    public  static final String FollowToCaptain=followAndblock+"Captain/"+"FollowToCaptain.php";
    public  static final String UnFollowToCaptain=followAndblock+"Captain/"+"UnFollowToCaptain.php";
    public  static final String BlockToCaptain=followAndblock+ "Captain/BlockToCaptain.php";
    public  static final String MyFolowersCaptain=followAndblock+"Captain/MyFollowers.php";
    public  static final String MyFolowingCaptain=followAndblock+"Captain/MyFollowing.php";
    public  static final String MyBlockCaptain=followAndblock+ "Captain/MyBlock.php";

    public static final String FollowToMarlet=followAndblock+"Market/FollowToMarlet.php";
    public static final String BlockToMarket=followAndblock+"Market/BlockToMarket.php";
    public static final String UnFollowToMarket=followAndblock+"Market/UnFollowToMarket.php";
    public static final String MyFolowers=followAndblock+"Market/MyFollowers.php";
    public static final String MyFolowing=followAndblock+"Market/MyFollowing.php";
    public static final String MyBlock=followAndblock+"Market/MyBlock.php";


    //Token Divice ..............
    private static final String DiviceToken=NAME_URL+"DiviceToken/";
    public static final String UpdateTokenMarket=DiviceToken+"UpdateTokenMarket.php";
    public static final String UpdateTokenCaptain=DiviceToken+"UpdateTokenCaptain.php";
    public static final String SignOutTokenCaptain=DiviceToken+"SignOutTokenCaptain.php";
    public static final String SignOutTokenMarket=DiviceToken+"SignOutTokenMarket.php";


    // Save Process : -->

    private static final String SaveProcess=NAME_URL+"SaveProcess/";
    public static final String SaveOfferMarket=SaveProcess+"SaveOfferMarketFree.php";
    public static final String SaveOfferCaptain=SaveProcess+"SaveOfferCaptainFree.php";
    public static final String AcceptOfferCaptain=SaveProcess+"AcceptOfferCaptain.php";
    public static final String SelectCurrentNowOffer=SaveProcess+"SelectNowOffer.php";
    public static final String EndSaveProcessOffer=SaveProcess+"EndSaveProcessOffer.php";


    // PyPal
    private static final String plan=NAME_URL+"plan/";
    public static final String viewplanvendor=plan+"marketPlan/getpackegmarket.php";
    public static final String SubscribeVendorPlan=plan+"marketPlan/Subscribe.php";

    //Get Pukeg CC
    public static  final String PackegCC=plan+"marketPlan/getccpackeg.php";

    public static final String viewplancaptain=plan+"captainPlan/getpackegcaptain.php";
    public static final String SubsicrebPlanCaptain=plan+"captainPlan/SubscribePlan.php";


    public static final String GetNotification="http://captain-care.org/ccapp/MyNotification/MyCaptain_Notification.php";


}
