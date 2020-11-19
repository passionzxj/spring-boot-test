//package com.zhang.web.controller;
//
//import cn.hutool.json.JSONUtil;
//import cn.hutool.system.UserInfo;
//import com.alibaba.fastjson.JSON;
//import com.zhang.word.WordResolver;
//import com.zhang.word.exception.ImportQuestionException;
//import com.zhang.word.question.AbstractQuestion;
//import com.zhang.word.question.SubQuestion;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.BeanUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class WordController {
//
//
//    @RequestMapping(value = "/upload", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseResult upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
//        ResponseResult responseResult = null;
//        InputStream inputStream = null;
//        try {
//            UserInfo userInfo = getRequestUserInfo();
//
//            inputStream = file.getInputStream();
//            String fileName = file.getOriginalFilename();
//            int splitIndex = fileName.lastIndexOf(".");
//            String prefix = fileName.substring(0,splitIndex);
//            String suffix = fileName.substring(splitIndex);
//            if(!".xml".equals(suffix)){
//                return ResponseResult.fail("请上传正确的文件（将word文件另存为xml格式）!");
//            }
//
//            String root = request.getSession().getServletContext().getRealPath("");
//            String path = root+"WEB-INF\\classes\\xsl";
//            WordResolver resolver = new WordResolver();
//            List<AbstractQuestion> questions = resolver.readXml(inputStream,path);
//
//            Date date = new Date();
//            RecordUpload recordUpload = getRecordUpload(userInfo, prefix, questions, date);
//
//
//
//            Map<String,Object> result = new HashMap<>();
//            result.put("count",questions.size());
//            responseResult = ResponseResult.success(result);
//
//            List<Integer> noKnowledgeQuestionIndexs = checkQuestionKnowledge(recordUpload).get("noKnowledge");
//            List<Integer> repetitionQuestionIndexs = checkQuestionKnowledge(recordUpload).get("repetitionKnowledge");
//            List<Integer> branchQuestionKnowledgeList = checkQuestionKnowledge(recordUpload).get("branchQuestionKnowledge");
//            noKnowledgeQuestionIndexs = noKnowledgeQuestionIndexs.stream().distinct().collect(Collectors.toList());
//            repetitionQuestionIndexs = repetitionQuestionIndexs.stream().distinct().collect(Collectors.toList());
//            StringBuffer msg1 = new StringBuffer();
//            for(int i=0;i<noKnowledgeQuestionIndexs.size();i++){
//                Integer questionIndex = noKnowledgeQuestionIndexs.get(i);
//                if(i>0){
//                    msg1.append(",");
//                }
//                msg1.append(questionIndex+1);
//            }
//            StringBuffer msg2 = new StringBuffer();
//            for(int i=0;i<repetitionQuestionIndexs.size();i++){
//                Integer questionIndex = repetitionQuestionIndexs.get(i);
//                if(i>0){
//                    msg2.append(",");
//                }
//                msg2.append(questionIndex+1);
//            }
//            StringBuffer msg3 = new StringBuffer();
//            for(int i=0;i<branchQuestionKnowledgeList.size();i++){
//                Integer branchQuestionKnowledgeIndex = branchQuestionKnowledgeList.get(i);
//                if(i>0){
//                    msg3.append(",");
//                }
//                msg3.append(branchQuestionKnowledgeIndex+1);
//            }
//
//            //将question里的知识点对象id为null的赋值为-1
//            List<QuestionV2> questionv2s = recordUpload.getQuestions();
//            for (QuestionV2 question : questionv2s) {
//                List<KnowledgeV2> v2List = question.getKnowledgeV2List();
//                for (KnowledgeV2 v2 : v2List) {
//                    if (v2.getId() == null) {
//                        v2.setId(-1L);
//                    }
//                }
//            }
//
//            if (recordUpload.getQuestions().size()>1){
//                if (noKnowledgeQuestionIndexs.size()>0) {
//                    responseResult.setMessage("导入成功，第["+msg1.toString()+"]题的知识点在系统中不存在，请前往系统编辑");
//                }
//            }else {
//                if(noKnowledgeQuestionIndexs.size()>0){
//                    if (branchQuestionKnowledgeList.size()>0){
//                        responseResult.setMessage("导入成功，第["+msg1.toString()+"]题的第("+msg3.toString()+")小题知识点在系统中不存在，请前往系统编辑");
//                    }
//                }
//            }
//            if(repetitionQuestionIndexs.size()>0){
//                throw new ImportQuestionException("导入失败，第["+msg2.toString()+"]题的知识点存在父子级冲突，请前往查看");
//            }
//
//            recordUploadService.save(recordUpload);
//        } catch (IOException e) {
//            e.printStackTrace();
//            responseResult = ResponseResult.fail("出异常了");
//        } catch (ImportQuestionException e){
//            String message = e.getMessage();
//            responseResult = ResponseResult.fail(message);
//        }catch(Exception e){
//            e.printStackTrace();
//            responseResult = ResponseResult.fail("导入模板格式有误，请整理模板后再导入！");
//        }finally {
//            if(inputStream != null){
//                inputStream.close();
//            }
//        }
//        return responseResult;
//    }
//
//    /**
//     * 检查知识点是否存在，存在父子级关系知识点的试题
//     * 如果其中某个QuestionV2的知识点在系统中不存在，返回改QuestionV2的index
//     * @param recordUpload
//     * @return
//     */
//    private Map<String,List<Integer>> checkQuestionKnowledge(RecordUpload recordUpload){
//        Map<String,List<Integer>> result = new HashMap<>();
//        List<Integer> list1 = new ArrayList<>();
//        List<Integer> list2 = new ArrayList<>();
//        List<Integer> list3 = new ArrayList<>();
//        List<QuestionV2> questionv2s = recordUpload.getQuestions();
//        for(int i=0;i<questionv2s.size();i++){
//            QuestionV2 question = questionv2s.get(i);
//            List<KnowledgeV2> v2s = question.getKnowledgeV2List();
//            if (question.getType()==1||question.getType()==2){
//                for (KnowledgeV2 v2 : v2s) {
//                    if (v2.getId() == null){
//                        list1.add(i);
//                    }
//                }
//                //一个试题中的知识点集合
//                Long[] knowledgeArr = v2s.stream().filter(e -> e.getId() != null && e.getId() != -1L).map(KnowledgeV2::getId).toArray(Long[]::new);
//                Map<String, Object> map = knowledgeV2Service.checkTheSelectedKnowledge(knowledgeArr);
//                if ((int)map.get("code")==500){
//                    list2.add(i);
//                }
//            }else { //一对多题型
//                for (int j = 0; j < v2s.size(); j++) {
//                    if (v2s.get(j).getId() == null){
//                        list1.add(i);
//                        list3.add(j);
//                    }
//                }
//            }
//
//        }
//        result.put("noKnowledge",list1);//知识点在系统中不存在的试题
//        result.put("repetitionKnowledge",list2);//存在父子级关系知识点的试题
//        result.put("branchQuestionKnowledge",list3);//一对多题型小题的知识点在系统中不存在的序号
//        return result;
//    }
//
//    private RecordUpload getRecordUpload(UserInfo userInfo, String title, List<AbstractQuestion> questions, Date date) throws ImportQuestionException {
//        List<QuestionV2> questionv2s = new ArrayList<>();
//        for(AbstractQuestion question:questions){
//            //类型转换 AbstractQuestion-->QuestionV2
//            QuestionV2 questionV2 = change2Question(question);
//
//            questionV2.setAnswer("该字段暂时未被使用，答案都保存在bizExt字段中");
//            questionV2.setCreatePin(userInfo.getUserName());
//            questionV2.setCreateTime(date);
//
//
//            questionV2.setModifyPin(userInfo.getUserName());
//            questionV2.setModifyTime(date);
//            questionV2.setQuestionTag(1);
//            questionV2.setSource(-1);
//            questionV2.setSourceName("");
//            questionv2s.add(questionV2);
//        }
//
//        RecordUpload recordUpload = new RecordUpload();
//        recordUpload.setCreatorId(userInfo.getUserId());
//        recordUpload.setCreateTime(date);
//        recordUpload.setModifyTime(date);
//        recordUpload.setStatus(0);
//        recordUpload.setCreator(userInfo.getUserName());
//        recordUpload.setTitle(title);
//
//        recordUpload.setQuestions(questionv2s);
//        return recordUpload;
//    }
//
//    private QuestionV2 change2Question(AbstractQuestion abstractQuestion) throws ImportQuestionException {
//        String title = abstractQuestion.getTopic();
//        String analysis = abstractQuestion.getAnalysis();
//        int type = abstractQuestion.getType();
//        List<SubQuestion> subQuestions = abstractQuestion.getSubQuestions();
//
//        String str = JSON.toJSONString(subQuestions);
//
//        QuestionV2 question = new QuestionV2();
//        question.setType((short)type);
//        question.setTitle(title);
//        question.setAnalysis(analysis);
//        question.setBizExt(str);
//        question.setDifficulty(abstractQuestion.calculateAverageDifficulty());
//
//        //设置学期和类型
//        String term = abstractQuestion.getTerm();
//        String scope = abstractQuestion.getScope();
//        if (StringUtils.isNotBlank(term)){
//            if (!term.equals("上学期")&&!term.equals("下学期")){
//                throw new ImportQuestionException("第"+abstractQuestion.getSeq()+"题请正确填写学期，如(上半期/下半期)");
//            }
//            question.setTerm(Objects.requireNonNull(TermEnum.getKeyByValue(term)).shortValue());
//        }
//        if (StringUtils.isNotBlank(scope)){
//            if (!scope.equals("月考")&&!scope.equals("期中")&&!scope.equals("期末")){
//                throw new ImportQuestionException("第"+abstractQuestion.getSeq()+"题请正确填写类型，如(月考/期中/期末)");
//            }
//            question.setScope(Objects.requireNonNull(ScopeEnum.getKeyByValue(scope)).shortValue());
//        }
//
//        //通过科目名称，查询科目id
//        String subjectName = abstractQuestion.getSubject();
//        SubjectV2 subjectV2 = subjectV2Service.findByName(subjectName);
//        if(subjectV2 == null){
//            throw new ImportQuestionException("科目错误");
//        }
//        question.setSubjectId(subjectV2.getId().intValue());
//        //通过年级名称，查询年级id
//        String gradeName = abstractQuestion.getGrade();
//        GradeV2 gradeV2 = gradeV2Service.findByName(gradeName);
//        if(gradeV2 == null){
//            throw new ImportQuestionException("年级错误");
//        }
//        question.setGrade(gradeV2.getId().shortValue());
//        //获取知识点的id
//        String knowledgePoint = abstractQuestion.getKnowledgePoint();
//        List<KnowledgeV2> knowledgeV2List = new ArrayList<>();
//        KnowledgeV2 knowledgeV2 = new KnowledgeV2();
//        if(StringUtils.isBlank(knowledgePoint)){
//            //模板不填知识点
//            knowledgeV2.setId(-1L);
//            knowledgeV2List.add(knowledgeV2);
//        }else{
//            //填写了知识点要校验是否已经存在该知识点
//            Long greadId = gradeV2.getId();
//            Long subjectId = subjectV2.getId();
//            Long middleId = knowledgeV2Service.findMiddleId(greadId, subjectId);
//            List<KnowledgeVO> result = knowledgeV2Service.findKnowledgeTree(middleId);
//            if(result == null || result.size() == 0){
//                knowledgeV2.setId(null);
//                knowledgeV2List.add(knowledgeV2);
//            }else{
//                List<KnowledgeVO> knowledgeVOList = manageKnowledge(type, question, knowledgePoint, result, abstractQuestion.getSeq());
//                if (CollectionUtils.isNotEmpty(knowledgeVOList)) {
//                    List<KnowledgeV2> collect = knowledgeVOList.stream().map(e -> {
//                        KnowledgeV2 v2 = new KnowledgeV2();
//                        BeanUtils.copyProperties(e, v2);
//                        return v2;
//                    }).collect(Collectors.toList());
//                    knowledgeV2List.addAll(collect);
//                }
//            }
//        }
//        question.setKnowledgeV2List(knowledgeV2List);
//        question.setKnowledgeId(-1L);//question表的knowledge_id字段不再维护
//        return question;
//    }
//
//    /**
//     * 知识点管理
//     * @param type
//     * @param question
//     * @param knowledgePoint
//     * @param result
//     * @return
//     */
//    private List<KnowledgeVO> manageKnowledge(int type, QuestionV2 question, String knowledgePoint, List<KnowledgeVO> result, int seq) throws ImportQuestionException {
//        List<String> knowledgeNameList = new ArrayList<>();
//        List<SubQuestion> subQuestionList = JSONUtil.toList(JSONUtil.parseArray(question.getBizExt()), SubQuestion.class);
//        if (type == 1 || type == 2){
//            //模板中一道题可能出现多个知识点,分割开后设置到question中
//            String[] knowledgeNameArr = knowledgePoint.split("&");
//            knowledgeNameList = Arrays.stream(knowledgeNameArr).map(String::trim).distinct().collect(Collectors.toCollection(ArrayList::new));
//        }else {
//            String substring = StringUtils.substringAfter(knowledgePoint, "&").replaceAll("\r|\n", "");
//            String[] split = substring.split("&");
//            for (int i = 0; i < split.length; i++) {
//                if (i<split.length-1){
//                    String trim = split[i].trim();
//                    if (i < 8){
//                        String knowledgeName = trim.substring(0, trim.length() - 1).trim();
//                        knowledgeNameList.add(knowledgeName);
//                    }else {
//                        String knowledgeName = trim.substring(0, trim.length() - 2).trim();
//                        knowledgeNameList.add(knowledgeName);
//                    }
//                }else {
//                    String knowledgeName = split[i].trim();
//                    knowledgeNameList.add(knowledgeName);
//                }
//            }
//
//            if (knowledgeNameList.size() != subQuestionList.size()) {
//                throw new ImportQuestionException("第" + seq + "题知识点个数与小题个数不一致，请核实");
//            }
//            List<Integer> collect = knowledgeNameList.stream()
//                    .map(e -> Optional.ofNullable(findByName(result, e)).orElse(new KnowledgeVO(-1L)).getId().intValue())
//                    .collect(Collectors.toList());
//
////            一对多题型模板中知识点非必填的话放开下面代码
////            if (collect.size()<subQuestionList.size()) {
////                int gap = subQuestionList.size() - collect.size();
////                for (int i = 0; i < gap; i++) {
////                    collect.add(-1);
////                }
////            }
//            for (int j = 0; j < subQuestionList.size(); j++) {
//                SubQuestion subQuestion = subQuestionList.get(j);
//                subQuestion.setKnowledgePoints(collect.get(j));
//            }
//            question.setBizExt(JSONUtil.toJsonStr(subQuestionList));
//        }
//        List<KnowledgeVO> knowledgeVOList = new ArrayList<>();
//        for (String knowledgeName : knowledgeNameList) {
//            KnowledgeVO knowledgeVO = findByName(result,knowledgeName);
//            if(knowledgeVO==null){
//                knowledgeVOList.add(new KnowledgeVO());
//            }else{
//                knowledgeVOList.add(knowledgeVO);
//            }
//        }
//        return knowledgeVOList;
//    }
//
//    private KnowledgeVO findByName(List<KnowledgeVO> knowledgeVOs,String name){
//        for(KnowledgeVO knowledgeVO:knowledgeVOs){
//            if(knowledgeVO.getName().equals(name)){
//                return knowledgeVO;
//            }
//
//            List<KnowledgeVO> childrens = knowledgeVO.getChildren();
//            if(childrens == null || childrens.size() == 0){
//                continue;
//            }
//            KnowledgeVO subResult = findByName(childrens,name);
//            if(subResult != null){
//                return subResult;
//            }
//
//        }
//        return null;
//    }
//
//    /**
//     * @catalog API接口文档/编辑试题
//     * @title 上传图片
//     * @description 上传图片
//     * @url /v2/back/word/uploadImg
//     * @method POST
//     * @param
//     * @return {"code":200,"message":null,"data":{"url": "http://10.0.0.118:90/file-view/group1/M00/00/00/CgBuF14EaCeAP8FCAAZGf4XaDj8387.png"}}
//     * @return_param code int 200(成功);500(失败/异常)
//     * @return_param message  上传成功返回消息,失败为空
//     * @return_param message.count 上传成功试题数目
//     *
//     */
//    /*@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
//    @ResponseBody
//    public Object uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
//        InputStream inputStream = null;
//        Map<String,Object> result = new HashMap<>();
//        try {
//
//            long size = file.getSize();
//            inputStream = file.getInputStream();
//
//            String fileName = file.getOriginalFilename();
//            int splitIndex = fileName.lastIndexOf(".");
//            String prefix = fileName.substring(0,splitIndex);
//            String suffix = fileName.substring(splitIndex);
//            if(!ImgConverter.isImgSuffix(suffix)){
//                return ResponseResult.fail("图片格式错误!");
//            }
//
//            byte[] data = IOUtils.toByteArray(inputStream);
//            String url = FastDFSClient.upload(data,fileName);
//
//            result.put("original",fileName);
//            result.put("size",size);
//            result.put("state","SUCCESS");
//            result.put("title",url);
//            result.put("type","."+suffix);
//            result.put("url",url);
//
//        } catch(Exception e){
//            e.printStackTrace();
//            result.put("state","FAIL");
//        }finally {
//            if(inputStream != null){
//                inputStream.close();
//            }
//        }
//        return result;
//    }*/
//}
