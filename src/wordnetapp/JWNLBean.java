package wordnetapp;

import java.io.FileInputStream;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;

import org.apache.log4j.*;

import net.didion.jwnl.*;
import net.didion.jwnl.data.*;
import net.didion.jwnl.data.list.*;
import net.didion.jwnl.dictionary.*;

public class JWNLBean {
	private static final Logger logger = Logger.getLogger(new RuntimeException()
												.getStackTrace()[0].getClassName());
    
	private String mFileProperties;
    private Dictionary mDictionary;
    private PointerUtils mPointerUtils;
    private PointerTargetTree mPointerTargetTree;
    
    JWNLBean() {
    	this.mFileProperties = "config/file_properties.xml";
    	initialize();
    }
    
    JWNLBean(String fileProperties) {
    	this.mFileProperties = fileProperties;
    	initialize();
    }
    
    void initialize(){
        try{
            JWNL.initialize(new FileInputStream(mFileProperties));
        }catch(Exception e){
            logger.debug(e);
        }
        
        logger.debug("success : JWNL Initialization");
        mDictionary = Dictionary.getInstance();
        mPointerUtils = PointerUtils.getInstance();
        logger.debug("success : mDictionary = " + mDictionary);
    }

    String findSynsets(String word, POS pos){
        String str = null;
        try{
             IndexWord indexWord = mDictionary.lookupIndexWord(pos, word);
             if(indexWord != null){
                str = indexWord.getPOS().getLabel() + "\n";
                for (int i = 1; i <= indexWord.getSenseCount(); i++){
                    Synset synset = indexWord.getSense(i);                    
                    logger.debug("SENSE " + i + " : " + synset.getGloss());                    
                    str += "SENSE " + i + " : " + synset.getGloss() + "\n";
                    str += parseSynset(synset);
                }
            }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    String findAllSynsets(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];
                logger.debug(indexWord.getPOS());
                str += indexWord.getPOS().getLabel() + "\n";
                for (int i = 1; i <= indexWord.getSenseCount(); i++){
                    Synset synset = indexWord.getSense(i);
                    logger.debug("SENSE " + i + " : " + synset.getGloss());
                    str += "SENSE " + i + " : " + synset.getGloss() + "\n";
                    str += parseSynset(synset);
                }
                str += "\n";
             }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    private String parseSynset(Synset synset){
        String str = null;
        Word[] wArr = synset.getWords();
        str = "\t";
        for(int j = 0 ; j < wArr.length; j++){
            logger.debug("\t" + wArr[j].getLemma().replace("_", " "));
            if(j > 0)
                str += ", ";
            str += wArr[j].getLemma().replace("_", " ");
        }
         str += "\n";
         return str;
    }
    
    String findHypernyms(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];

                logger.debug(indexWord.getPOS());
                str += "\nas " + indexWord.getPOS().getLabel() + " Hypernyms are -" + "\n";
                for (int i = 1; i <= indexWord.getSenseCount(); i++){
                    Synset synset = indexWord.getSense(i);
                    //if(indexWord.getPOS() == POS.NOUN){
                         mPointerTargetTree = mPointerUtils.getHypernymTree(synset);
                         List list = mPointerTargetTree.toList();
                         Iterator it = list.iterator();
                         while(it.hasNext()){
                             PointerTargetNodeList nodeList = (PointerTargetNodeList)it.next();

                             if(nodeList.size() > 1){
                                 Iterator iter = nodeList.iterator();
                                 str += "SENSE " + i + ": "+synset.getGloss()+"\n";
                                 String tmp = "   ";
                                 str += tmp + parseSynset(((PointerTargetNode)iter.next())
                                     .getSynset()).substring(1);
                                 while(iter.hasNext()){
                                     PointerTargetNode ptn1 = (PointerTargetNode)iter.next();
                                     tmp += "   ";
                                     str += tmp  + "=>" + parseSynset(ptn1.getSynset()).substring(1);
                                }
                             }
                         }
                    //}
                }
             }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    String findHyponyms(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];

                logger.debug(indexWord.getPOS());
                str += "\nas " + indexWord.getPOS().getLabel() + " Hyponyms are -" + "\n";
                for (int i = 1; i <= indexWord.getSenseCount(); i++){
                    Synset synset = indexWord.getSense(i);
                    //if(indexWord.getPOS() == POS.NOUN){
                         mPointerTargetTree = mPointerUtils.getHyponymTree(synset);
                         List list = mPointerTargetTree.toList();
                         Iterator it = list.iterator();
                         while(it.hasNext()){
                             PointerTargetNodeList nodeList = (PointerTargetNodeList)it.next();

                             if(nodeList.size() > 1){
                                Iterator iter = nodeList.iterator();
                                str += "SENSE " + i + ": "+synset.getGloss()+"\n";
                                String tmp = "   ";
                                str += tmp + parseSynset(((PointerTargetNode)iter.next())
                                     .getSynset()).substring(1);
                                while(iter.hasNext()){
                                    PointerTargetNode ptn1 = (PointerTargetNode)iter.next();
                                    tmp += "   ";
                                    str += tmp  + "=>" + parseSynset(ptn1.getSynset()).substring(1);
                                }
                             }
                         }
                    //}
                }
             }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    String findMeronyms(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];

                logger.debug(indexWord.getPOS());
                str += "\nas " + indexWord.getPOS().getLabel() + " Meronyms are -" + "\n";
                for (int i = 1; i <= indexWord.getSenseCount(); i++){
                    Synset synset = indexWord.getSense(i);
                    PointerTargetNodeList ptnl = mPointerUtils.getPartMeronyms(synset);
                    if(ptnl.size() > 0)
                         str += "SENSE " + i + ": " + synset.getGloss() +"\n";
                    Iterator it = ptnl.iterator();
                    while(it.hasNext()){
                        PointerTargetNode ptn = (PointerTargetNode)it.next();
                        str += "   HAS PART: " + parseSynset(ptn.getSynset()).substring(1);
                    }
                    ptnl = mPointerUtils.getMemberMeronyms(synset);
                    if(ptnl.size() > 0)
                         str += "SENSE " + i + ": " + synset.getGloss() +"\n";
                    it = ptnl.iterator();
                    while(it.hasNext()){
                        PointerTargetNode ptn = (PointerTargetNode)it.next();
                        str += "   HAS MEMBER: " + parseSynset(ptn.getSynset()).substring(1);
                    }
                    ptnl = mPointerUtils.getSubstanceMeronyms(synset);
                    if(ptnl.size() > 0)
                         str += "SENSE " + i + ": " + synset.getGloss() +"\n";
                    it = ptnl.iterator();
                    while(it.hasNext()){
                        PointerTargetNode ptn = (PointerTargetNode)it.next();
                        str += "   HAS SUBSTANCE: " + parseSynset(ptn.getSynset()).substring(1);
                    }
                }
             }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    String findHolonyms(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];

                logger.debug(indexWord.getPOS());
                str += "\nas " + indexWord.getPOS().getLabel() + " Holonyms are -" + "\n";
                for (int i = 1; i <= indexWord.getSenseCount(); i++){
                    Synset synset = indexWord.getSense(i);
                    PointerTargetNodeList ptnl = mPointerUtils.getPartHolonyms(synset);
                    if(ptnl.size() > 0)
                         str += "SENSE " + i + ": " + synset.getGloss() +"\n";
                    Iterator it = ptnl.iterator();
                    while(it.hasNext()){
                        PointerTargetNode ptn = (PointerTargetNode)it.next();
                        str += "   PART OF: " + parseSynset(ptn.getSynset()).substring(1);
                    }
                    ptnl = mPointerUtils.getMemberHolonyms(synset);
                    if(ptnl.size() > 0)
                         str += "SENSE " + i + ": " + synset.getGloss() +"\n";
                    it = ptnl.iterator();
                    while(it.hasNext()){
                        PointerTargetNode ptn = (PointerTargetNode)it.next();
                        str += "   MEMBER OF: " + parseSynset(ptn.getSynset()).substring(1);
                    }
                    ptnl = mPointerUtils.getSubstanceHolonyms(synset);
                    if(ptnl.size() > 0)
                         str += "SENSE " + i + ": " + synset.getGloss() +"\n";
                    it = ptnl.iterator();
                    while(it.hasNext()){
                        PointerTargetNode ptn = (PointerTargetNode)it.next();
                        str += "   SUBSTANCE OF: " + parseSynset(ptn.getSynset()).substring(1);
                    }
                }
             }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    String findCoordTerms(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];

                logger.debug(indexWord.getPOS());
                str += "\nas " + indexWord.getPOS().getLabel() + " Coordinate Terms are -" + "\n";
                for (int i = 1; i <= indexWord.getSenseCount(); i++){
                    Synset synset = indexWord.getSense(i);
                    PointerTargetNodeList ptnl = mPointerUtils.getCoordinateTerms(synset);
                    if(ptnl.size() > 0)
                         str += "\nSENSE " + i + ": " + synset.getGloss() +"\n";
                    Iterator it = ptnl.iterator();
                    while(it.hasNext()){
                        PointerTargetNode ptn = (PointerTargetNode)it.next();
                        str += "    =>  " + parseSynset(ptn.getSynset()).substring(1);
                    }
                }
             }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    String findEntail(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];

                if(indexWord.getPOS() == POS.VERB){
                    str += "\nas " + indexWord.getPOS().getLabel() + " Entailments are -" + "\n";
                    for (int i = 1; i <= indexWord.getSenseCount(); i++){
                        Synset synset = indexWord.getSense(i);
                         mPointerTargetTree = mPointerUtils.getEntailmentTree(synset);
                         List list = mPointerTargetTree.toList();
                         Iterator it = list.iterator();
                         while(it.hasNext()){
                             PointerTargetNodeList nodeList = (PointerTargetNodeList)it.next();

                             if(nodeList.size() > 1){
                                 Iterator iter = nodeList.iterator();
                                 str += "SENSE " + i + ": "+synset.getGloss()+"\n";
                                 String tmp = "   ";
                                 str += tmp + parseSynset(((PointerTargetNode)iter.next())
                                     .getSynset()).substring(1);
                                 while(iter.hasNext()){
                                     PointerTargetNode ptn1 = (PointerTargetNode)iter.next();
                                     tmp += "   ";
                                     str += tmp  + "=>" + parseSynset(ptn1.getSynset()).substring(1);
                                }
                             }
                         }
                    }
                }
             }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    String findEntailBy(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];

                if(indexWord.getPOS() == POS.VERB){
                    str += "\nas " + indexWord.getPOS().getLabel() + " Entailed By -" + "\n";
                    for (int i = 1; i <= indexWord.getSenseCount(); i++){
                        Synset synset = indexWord.getSense(i);
                         mPointerTargetTree = mPointerUtils.getEntailedByTree(synset);
                         List list = mPointerTargetTree.toList();
                         Iterator it = list.iterator();
                         while(it.hasNext()){
                             PointerTargetNodeList nodeList = (PointerTargetNodeList)it.next();

                             if(nodeList.size() > 1){
                                 Iterator iter = nodeList.iterator();
                                 str += "SENSE " + i + ": "+synset.getGloss()+"\n";
                                 String tmp = "   ";
                                 str += tmp + parseSynset(((PointerTargetNode)iter.next())
                                     .getSynset()).substring(1);
                                 while(iter.hasNext()){
                                     PointerTargetNode ptn1 = (PointerTargetNode)iter.next();
                                     tmp += "   ";
                                     str += tmp  + "=>" + parseSynset(ptn1.getSynset()).substring(1);
                                }
                             }
                         }
                    }
                }
             }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    String findCause(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];

                if(indexWord.getPOS() == POS.VERB){
                    str += "\nas " + indexWord.getPOS().getLabel() + " Causation By -" + "\n";
                    for (int i = 1; i <= indexWord.getSenseCount(); i++){
                        Synset synset = indexWord.getSense(i);
                         mPointerTargetTree = mPointerUtils.getCauseTree(synset);
                         List list = mPointerTargetTree.toList();
                         Iterator it = list.iterator();
                         while(it.hasNext()){
                             PointerTargetNodeList nodeList = (PointerTargetNodeList)it.next();

                             if(nodeList.size() > 1){
                                 Iterator iter = nodeList.iterator();
                                 str += "SENSE " + i + ": "+synset.getGloss()+"\n";
                                 String tmp = "   ";
                                 str += tmp + parseSynset(((PointerTargetNode)iter.next())
                                     .getSynset()).substring(1);
                                 while(iter.hasNext()){
                                     PointerTargetNode ptn1 = (PointerTargetNode)iter.next();
                                     tmp += "   ";
                                     str += tmp  + "=>" + parseSynset(ptn1.getSynset()).substring(1);
                                }
                             }
                         }
                    }
                }
             }
        }catch(Exception e){
            logger.debug(e);
        }
        return str;
    }

    String findAntonyms(String word){
        IndexWordSet indexWordSet;
        String str = null;
        try{
             indexWordSet = mDictionary.lookupAllIndexWords(word);
             logger.debug(indexWordSet);
             
             str = indexWordSet.toString() + "\n";
             IndexWord[] indexWords = indexWordSet.getIndexWordArray();
             
             for(int k = 0; k < indexWords.length; k++){
                IndexWord indexWord = indexWords[k];
                logger.debug(indexWord.getPOS());
                
                str += "\nas " + indexWord.getPOS().getLabel() + " Antonyms are -" + "\n";
                for (int i = 1; i <= indexWord.getSenseCount(); i++){
                    Synset synset = indexWord.getSense(i);
                    
                    PointerTargetNodeList nodeList = mPointerUtils.getAntonyms(synset);
                    if(nodeList.size() > 0){
                        str += "SENSE " + i + ": " + synset.getGloss() +"\n";
                        Iterator iterator = nodeList.iterator();
                        String tmp = "   ";
                        
                        while(iterator.hasNext()){
                            Synset s1 = ((PointerTargetNode)iterator.next()).getSynset();                        
                            str += tmp + parseSynset(s1).substring(1);
                        }
                    }

                    mPointerTargetTree = mPointerUtils.getExtendedAntonyms(synset);
                    List list = mPointerTargetTree.toList();
                    Iterator it = list.iterator();
                    if (((PointerTargetNodeList)list.get(0)).size() > 2)
                        str += "EXTENDED SENSE " + i +": " + synset.getGloss()+ "\n";
                    while(it.hasNext()){
                        nodeList = (PointerTargetNodeList)it.next();
                        if(nodeList.size() > 2){
                            Iterator iter = nodeList.iterator();
                            String tmp = "   ";
                            str += tmp + parseSynset(((PointerTargetNode)iter.next())
                                     							.getSynset()).substring(1);
                          
                            while(iter.hasNext()){
                                 PointerTargetNode ptn1 = (PointerTargetNode)iter.next();
                                 tmp += "   ";
                                 str += tmp  + "=>" + parseSynset(ptn1.getSynset()).substring(1);
                            }
                        }
                    }
                    mPointerTargetTree = mPointerUtils.getIndirectAntonyms(synset);
                    list = mPointerTargetTree.toList();
                    it = list.iterator();
                    if (((PointerTargetNodeList)list.get(0)).size() > 2)
                        str += "INDIRECT SENSE " + i +": " + synset.getGloss()+ "\n";
                    while(it.hasNext()){
                        nodeList = (PointerTargetNodeList)it.next();
                        if(nodeList.size() > 2){
                            ListIterator iter = nodeList.listIterator();
                            String tmp = "   ";
                            str += tmp + parseSynset(((PointerTargetNode)iter.next())
                                     .getSynset()).substring(1);
                            while(iter.hasNext()){
                                 PointerTargetNode ptn1 = (PointerTargetNode)iter.next();
                                 tmp += "   ";
                                 str += tmp  + "=>" + parseSynset(ptn1.getSynset()).substring(1);
                            }
                        }
                    }
                }
             }
        }
        catch(Exception e){
            logger.debug(e);
        }
        
        return str;
    }

    public static void main(String[] arg){
        JWNLBean bean = new JWNLBean();
        logger.debug(bean.findCause("succeed"));
        logger.debug(bean.findAllSynsets("go"));

    }
}
