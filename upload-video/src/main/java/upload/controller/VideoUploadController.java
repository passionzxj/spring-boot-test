package upload.controller;

import org.csource.fastdfs.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upload.service.VideoSaveService;
import upload.util.AjaxResult;
import upload.util.FastDFSClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("file")
public class VideoUploadController {

    @Autowired
    private VideoSaveService videoSaveService;


    @GetMapping("/get")
    @ResponseBody
    public AjaxResult get(){
        return AjaxResult.success("success");
    }

    @PostMapping("/uploadVideo")
    @ResponseBody
    public AjaxResult uploadVideo(@RequestParam("file") MultipartFile file,HttpServletRequest request,
                              @RequestParam(value="userId",defaultValue="1") Integer userId,
                              @RequestParam(value="professionalName",defaultValue="hhh") String professionalName,
                              @RequestParam(value="title",defaultValue="123") String title,
                              @RequestParam(value="collectionCount",defaultValue="0") Integer collectionCount,
                              @RequestParam(value="thumbCount",defaultValue="0") Integer thumbCount,
                              @RequestParam(value="visitCount",defaultValue="0") Integer visitCount,
                              @RequestParam(value="commentCount",defaultValue="0") Integer commentCount
                              ) throws Exception {
        if(videoSaveService.insertVideo(file,userId,professionalName,title,collectionCount,thumbCount,visitCount,commentCount)==1){
            HttpSession session = request.getSession();
            Object percent = session.getAttribute("upload_percent");
            int progress =  null != percent ? (Integer) percent : 0;
            return AjaxResult.success(progress);
        }else {
            return AjaxResult.success("success");
        }

    }

    @GetMapping(value = "/uploadStatus")
    @ResponseBody
    public Integer uploadStatus(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object percent = session.getAttribute("upload_percent");
        return null != percent ? (Integer) percent : 0;
    }


    @DeleteMapping("del")
    @ResponseBody
    public AjaxResult deleteFile(@RequestParam String groupName,@RequestParam String fileName) throws Exception{
        FastDFSClient.deleteFile(groupName,fileName);
        return AjaxResult.success("");
    }
    @GetMapping("getFile")
    @ResponseBody
    public AjaxResult getFile(@RequestParam String groupName,@RequestParam String fileName) throws Exception{
        FileInfo file = FastDFSClient.getFile(groupName, fileName);
        return AjaxResult.success(file);
    }
}