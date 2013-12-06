package com.RingWorks.videolooper;

public class Errorno{	

    // error no

    public static final int PLAYER_SUCCESS 			=	0;
    public static final int PLAYER_FAILED  			=	-0x02000001;
    public static final int PLAYER_NOMEM   			=   -0x02000002;
    public static final int PLAYER_EMPTY_P 			=   -0x02000003;
    public static final int PLAYER_NOT_VALID_PID 	=   -0x02000004;
    public static final int PLAYER_CAN_NOT_CREAT_THREADS =   -0x02000005;
    public static final int PLAYER_ERROR_PARAM 		=   -0x02000006;

    public static final int PLAYER_RD_FAILED 		=   -0x02000011;
    public static final int PLAYER_RD_EMPTYP 		=   -0x02000012;
    public static final int PLAYER_RD_TIMEOUT 		=   -0x02000013;
    public static final int PLAYER_RD_AGAIN 		=   -0x02000014;

    public static final int PLAYER_WR_FAILED 		=   -0x02000021;
    public static final int PLAYER_WR_EMPTYP 		=   -0x02000022;
    public static final int PLAYER_WR_FINISH 		=   0x020000024;

    public static final int PLAYER_PTS_ERROR 		=   -0x02000031;
    public static final int PLAYER_NO_DECODER 		=   -0x02000032;
    public static final int DECODER_RESET_FAILED 	=   -0x02000033;
    public static final int DECODER_INIT_FAILED 	=   -0x02000034;
    public static final int PLAYER_UNSUPPORT 		=   -0x02000035;
    public static final int PLAYER_UNSUPPORT_VIDEO 	= 	-0x02000036;
    public static final int PLAYER_UNSUPPORT_AUDIO 	= 	-0x02000037;
    public static final int PLAYER_SEEK_OVERSPILL  	= 	-0x02000038;
    public static final int PLAYER_CHECK_CODEC_ERROR = 	-0x02000039;
    public static final int PLAYER_INVALID_CMD 		=   -0x02000040;

    public static final int PLAYER_REAL_AUDIO_FAILED = 	-0x02000041;
	public static final int PLAYER_ADTS_NOIDX		=	-0x02000042;
	public static final int PLAYER_SEEK_FAILED		= 	-0x02000043;
	public static final int PLAYER_NO_VIDEO  		= 	-0x02000044;
	public static final int PLAYER_NO_AUDIO  		= 	-0x02000045;
	public static final int PLAYER_SET_NOVIDEO  	= 	-0x02000046;
	public static final int PLAYER_SET_NOAUDIO  	= 	-0x02000047;
	public static final int PLAYER_FFFB_UNSUPPORT   =   -0x02000048;
	public static final int PLAYER_UNSUPPORT_VCODEC =   -0x02000049;

    public static final int FFMPEG_SUCCESS 			=   0;
    public static final int FFMPEG_OPEN_FAILED 		=   -0x03000001;
    public static final int FFMPEG_PARSE_FAILED 	=   -0x03000002;
    public static final int FFMPEG_EMP_POINTER 		=	-0x03000003;
    public static final int FFMPEG_NO_FILE 			=   -0x03000004;
    
    public static final int DIVX_SUCCESS            =   0;
    public static final int DIVX_AUTHOR_ERR         =   -0x04000001;

    public static String getErrorInfo(int errID)
    {
    	String errStr = null;		
    	switch (errID){
    	case Errorno.PLAYER_SUCCESS:
    		errStr="no error";
    		break;
    	case Errorno.PLAYER_FAILED:
    		errStr="error:player normal error";
    		break;
    	case Errorno.PLAYER_NOMEM:
    		errStr="error:can't allocate enough memory";
    		break;
    	case Errorno.PLAYER_EMPTY_P:
    		errStr="error:Invalid pointer";
    		break;
    	case Errorno.PLAYER_CAN_NOT_CREAT_THREADS:
    		errStr="error: player create thread failed";
    		break;
    	case Errorno.PLAYER_ERROR_PARAM:
    		errStr="error:Invalid parameter for player";
    		break;
    		
    	case Errorno.PLAYER_RD_FAILED:
    		errStr="error:player read file error";
    		break;
    	case Errorno.PLAYER_RD_EMPTYP:
    		errStr="error:invalid pointer when reading";
    		break;
    	case Errorno.PLAYER_RD_TIMEOUT:
    		errStr="error:no data for reading,time out";
    		break;
    	case Errorno.PLAYER_RD_AGAIN:
    		errStr="warning:no data, need read again";
    		break;
    		
    		
    	case Errorno.PLAYER_WR_EMPTYP:
    		errStr="error:invalid pointer when writing";
    		break;
    	case Errorno.PLAYER_WR_FAILED:
    		errStr="error:player write data error";
    		break;
    	case Errorno.PLAYER_WR_FINISH:
    		errStr="error:player write finish";
    		break;
    		
    	case Errorno.PLAYER_PTS_ERROR:
    		errStr="error:player pts error";
    		break;
    	case Errorno.PLAYER_NO_DECODER:
    		errStr="error:can't find valid decoder";
    		break;
    	case Errorno.DECODER_RESET_FAILED:
    		errStr="error:decoder reset failed";
    		break;
    	case Errorno.DECODER_INIT_FAILED:
    		errStr="error:decoder init failed";
    		break;
    	case Errorno.PLAYER_UNSUPPORT:
    		errStr="error:player unsupport file type";
    		break;
    	case Errorno.PLAYER_UNSUPPORT_VIDEO:
    		errStr="warning:video format can't support yet";
    		break;
    	case Errorno.PLAYER_UNSUPPORT_AUDIO:
    		errStr="warning:audio format can't support yet";
    		break;
    	case Errorno.PLAYER_SEEK_OVERSPILL:
    		errStr="warning:seek time point overspill";
    		break;
    	case Errorno.PLAYER_CHECK_CODEC_ERROR:
    		errStr="error:check codec status error";
    		break;
    	case Errorno.PLAYER_INVALID_CMD:
    		errStr="warning:invalid command under current status";
    		break;
    		
    	case Errorno.PLAYER_REAL_AUDIO_FAILED:
    		errStr="error: real audio failed";
    		break;
    	case Errorno.PLAYER_ADTS_NOIDX:
    		errStr="error:adts audio index invalid";
    		break;
    	case Errorno.PLAYER_SEEK_FAILED:
    		errStr="error:seek file failed";
    		break;
    	case Errorno.PLAYER_NO_VIDEO:
    		errStr="warning:file without video stream";
    		break;
    	case Errorno.PLAYER_NO_AUDIO:
    		errStr="warning:file without audio stream";
    		break;
    	case Errorno.PLAYER_SET_NOVIDEO:
    		errStr="warning:user set playback without video";
    		break;
    	case Errorno.PLAYER_SET_NOAUDIO:
    		errStr="warning:user set playback without audio";
    		break;
    	case Errorno.PLAYER_UNSUPPORT_VCODEC:
    		errStr="error:unsupport video codec";
    		break;
    	case Errorno.FFMPEG_OPEN_FAILED:
    		
    		errStr="error:can't open input file";
    		break;
    	case Errorno.FFMPEG_PARSE_FAILED:
    		errStr="error:parse file failed";
    		break;
    	case Errorno.FFMPEG_EMP_POINTER:
    		errStr="error:check invalid pointer";
    		break;
    	case Errorno.FFMPEG_NO_FILE:
    		errStr="error:not assigned a file to play";
    		break;
    	}
    	return errStr;

    }

}