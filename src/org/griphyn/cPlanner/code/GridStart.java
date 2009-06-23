/**
 *  Copyright 2007-2008 University Of Southern California
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package org.griphyn.cPlanner.code;


import org.griphyn.cPlanner.classes.ADag;
import org.griphyn.cPlanner.classes.PegasusBag;
import org.griphyn.cPlanner.classes.SubInfo;
import org.griphyn.cPlanner.classes.AggregatedJob;

import java.util.Collection;

import java.io.File;

/**
 * The interface that defines how a job specified in the abstract workflow
 * is launched on the grid. This allows to specify different ways to wrap
 * an executable while running on the grid. One may do this, to gather
 * additional information about the job like provenance information.
 *
 * If the implementation returns true for canSetXBit, then it should be setting
 * the X bit for the staged compute jobs.
 *
 * @author Karan Vahi vahi@isi.edu
 * @version $Revision$
 */
public interface  GridStart {


    /**
     * The version number associated with this API of GridStart.
     */
    public static final String VERSION = "1.5";

    /**
     * The File separator to be used on the submit host.
     */
    public static char mSeparator = File.separatorChar;


    /**
     * Initializes the GridStart implementation.
     *
     * @param bag   the bag of objects that is used for initialization.
     * @param dag   the concrete dag so far.
     */
    public void initialize( PegasusBag bag, ADag dag );

    /**
     * Enables a collection of jobs and puts them into an AggregatedJob.
     * The assumption here is that all the jobs are being enabled by the same
     * implementation. It is upto the implementation to determine whether it
     * wants to return a new AggregatedJob by cloning the one passed or just
     * modify the one passed to it.
     *
     * @param aggJob the AggregatedJob into which the collection has to be
     *               integrated.
     * @param jobs   the collection of jobs (SubInfo) that need to be enabled.
     *
     * @return the AggregatedJob containing the enabled jobs.
     * @see #enable(SubInfo,boolean)
     */
    public  AggregatedJob enable(AggregatedJob aggJob,Collection jobs);

    /**
     * Enables a job to run on the grid. This also determines how the
     * stdin,stderr and stdout of the job are to be propogated.
     * To grid enable a job, the job may need to be wrapped into another
     * job, that actually launches the job. It usually results in the job
     * description passed being modified modified.
     *
     * @param job  the <code>SubInfo</code> object containing the job description
     *             of the job that has to be enabled on the grid.
     * @param isGlobusJob is <code>true</code>, if the job generated a
     *        line <code>universe = globus</code>, and thus runs remotely.
     *        Set to <code>false</code>, if the job runs on the submit
     *        host in any way.
     *
     * @return boolean true if enabling was successful,else false.
     */
    public boolean enable(SubInfo job,boolean isGlobusJob);

    /**
     * Constructs the postscript that has to be invoked on the submit host
     * after the job has executed on the remote end. The postscript usually
     * works on the output generated by the executable that is used to grid
     * enable a job, and has been piped back by Condor.
     * <p>
     * The postscript should be constructed and populated as a profile
     * in the DAGMAN namespace.
     *
     *
     * @param job  the <code>SubInfo</code> object containing the job description
     *             of the job that has to be enabled on the grid.
     * @param key  the key for the profile that has to be inserted.
     *
     * @return boolean true if postscript was generated,else false.
     */
//    public boolean constructPostScript( SubInfo job, String key ) ;

    /**
     * Indicates whether the enabling mechanism can set the X bit
     * on the executable on the remote grid site, in addition to launching
     * it on the remote grid stie
     *
     * @return boolean indicating whether can set the X bit or not.
     */
    public boolean canSetXBit();

    /**
     * Returns the directory in which the job executes on the worker node.
     * 
     * @param job
     * 
     * @return  the full path to the directory where the job executes
     */
    public String getWorkerNodeDirectory( SubInfo job );

    
    /**
     * Returns the value of the vds profile with key as VDS.GRIDSTART_KEY,
     * that would result in the loading of this particular implementation.
     * It is usually the name of the implementing class without the
     * package name.
     *
     * @return the value of the profile key.
     * @see org.griphyn.cPlanner.namespace.VDS#GRIDSTART_KEY
     */
    public String getVDSKeyValue();

    /**
     * Returns a short textual description of the implementing class.
     * Should usually be the name of the implementing class.
     *
     * @return  short textual description.
     */
    public String shortDescribe();

    /**
     * Returns the SHORT_NAME for the POSTScript implementation that is used
     * to be as default with this GridStart implementation.
     *
     * @return the id for the POSTScript.
     *
     * @see POSTScript#shortDescribe()
     */
    public String defaultPOSTScript();

    /**
    * Returns the full path to the submit directory, for the job.
    *
    * @param root the base of the submit directory hierarchy for the workflow.
    * @param job  the job for which the submit directory is to be determined.
    *
    * @return the path to the submit directory.
    */
//    public static String getSubmitDirectory( String root, SubInfo job ){
//        String jobDir   = job.getSubmitDirectory();
//        StringBuffer sb = new StringBuffer();
//
//        //some sanity checks
//        if( jobDir == null && root == null){
//            throw new NullPointerException(
//                         "Both the root directory, and job directory are null");
//        }
//
//
//        //determine the submit directory for the job
//        if(jobDir == null){
//            sb.append(root);
//        }
//        else if(jobDir.indexOf(mSeparator) == 0){
//            //absolute path use that
//            sb.append(jobDir);
//        }
//        else{
//            //handle the . if given
//            sb.append(root).append(mSeparator);
//            sb.append((jobDir.indexOf('.') ==  0)?
//                            //handle separator if given
//                            (jobDir.indexOf(mSeparator) == 1)?
//                                   jobDir.substring(2):jobDir.substring(1)
//                            //just append whatever is given
//                            :jobDir);
//        }
//
//        return sb.toString();
//    }


}
