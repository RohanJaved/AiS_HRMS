package com.appinsnap.aishrm.ui.fragments.Policies

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.databinding.FragmentPoliciesBinding
import com.appinsnap.aishrm.ui.fragments.Policies.adapters.PoliciesCategoriesAdapter
import com.appinsnap.aishrm.ui.fragments.Policies.adapters.PolicyAdapter
import com.appinsnap.aishrm.ui.fragments.Policies.adapters.SOPAdapter
import com.appinsnap.aishrm.ui.fragments.Policies.adapters.SOPCategoriesAdapter
import com.appinsnap.aishrm.ui.fragments.Policies.models.Attachmentlist
import com.appinsnap.aishrm.ui.fragments.Policies.models.GetPoliciesType
import com.appinsnap.aishrm.ui.fragments.Policies.models.GetPoliciesresponse
import com.appinsnap.aishrm.ui.fragments.Policies.models.GetSOPSType
import com.appinsnap.aishrm.ui.fragments.Policies.models.GetSOPresponse
import com.appinsnap.aishrm.ui.fragments.Policies.models.PDFInfo
import com.appinsnap.aishrm.ui.fragments.Policies.models.PoliciesSOPResponse
import com.appinsnap.aishrm.ui.fragments.Policies.models.SopAttachmentlist
import com.appinsnap.aishrm.ui.fragments.home.viewmodel.HomeViewModel
import com.appinsnap.aishrm.util.GeneralDialogFragment
import com.appinsnap.aishrm.util.NetworkResult
import com.appinsnap.aishrm.util.SessionManagement
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@AndroidEntryPoint
class PoliciesFragment : GeneralDialogFragment(), GeneralDialogFragment.onClick,
    GeneralDialogFragment.locationPermission,PoliciesCategoriesAdapter.onCategoryPolicyClick,PolicyAdapter.onPolicyClick,SOPCategoriesAdapter.onCategorySOPClick,SOPAdapter.onSOPClick {
    private var _binding: FragmentPoliciesBinding?=null
    private var searchviewemployeeenable = false
    private val binding get() = _binding!!
    var newPolicyDataList:ArrayList<GetPoliciesresponse> = ArrayList()
    var newSOPDataList:ArrayList<GetSOPresponse> = ArrayList()
    private var sopcategoryType:String="all"
    private var policycategoryType:String="all"
    private var sessionmanagement: SessionManagement? = null
    private var policieslist:ArrayList<GetPoliciesresponse> = ArrayList()
    private var sopslist:ArrayList<GetSOPresponse> = ArrayList()
    private var searchCategoryList:ArrayList<GetPoliciesresponse> = ArrayList()
    private var searchSOPCategoryList:ArrayList<GetSOPresponse> = ArrayList()
    private var searchSopAttachmentlist:ArrayList<SopAttachmentlist> = ArrayList()
    private var searchPolicyAttachment:ArrayList<Attachmentlist> = ArrayList()
    private var policiessopresponse:NetworkResult.Success<PoliciesSOPResponse>?=null
    private var policyCategory:ArrayList<GetPoliciesType> = ArrayList()
    private var sopCategory:ArrayList<GetSOPSType> = ArrayList()
    private var searchPolicyList:ArrayList<GetPoliciesresponse> = ArrayList()
    private var searchSOPList:ArrayList<GetSOPresponse> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
        ).get(HomeViewModel::class.java)
    }
    private lateinit var progressBarDialogRegister: ProgressBarDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_policies, container, false)
        val view = binding.root
        sessionmanagement = SessionManagement(requireContext())
        progressBarDialogRegister = ProgressBarDialog(requireContext())
        // Inflate the layout for this fragment
       // changePolicyLayout()
        showLastSelectedTab()
        callPoliciesSOPApi()
        policySearchHandler()
        sopSearchHandler()
        return view
    }

    private fun showLastSelectedTab() {
        var type = sessionmanagement?.getPDFViewTab()
        if(type=="sop")
        {
            changeSOPLayout()
        }
        else{
            changePolicyLayout()
        }
    }

    private fun sopSearchHandler() {
        val addedSOPNames = HashSet<String>()
        binding.idSVSOP.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.idSVSOP.setHint("")
            false
        })

        binding.idSVSOP.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when the text is being changed
                if (s.toString() == null || s.toString().isEmpty()) {
                    addedSOPNames.clear()
                    searchSOPCategoryList.clear()
                    searchSopAttachmentlist.clear()
                    binding.txtnorecordfoundemployeesop.hide()
                    if (searchSOPCategoryList.isNotEmpty()) {
                        searchSOPCategoryList.clear()
                    }
                    searchSOPList = newSOPDataList
                    setSOPAdapter(searchSOPList)

                } else {
                    addedSOPNames.clear()
                    searchviewemployeeenable = true
                    searchSOPCategoryList.clear()
                    searchSopAttachmentlist.clear()

                    for (i in searchSOPList.indices) {
                        for (j in searchSOPList[i].sopAttachmentlist.indices)
                            if ((searchSOPList != null && searchSOPList.get(i).name.lowercase()
                                    .startsWith(s.toString().lowercase()))
                            ) {

                                if (addedSOPNames.add(searchSOPList[i].name)) {
                                    // SOP name not added before, add to searchSOPCategoryList
                                    binding.txtnorecordfoundemployeesop.hide()
                                    searchSOPCategoryList.add(searchSOPList[i])
                                }
                              }
                             else if (searchSOPList[i].sopAttachmentlist != null && searchSOPList[i].sopAttachmentlist[j].title.lowercase()
                                    .contains(s.toString().lowercase())
                            ) {
                                 addedSOPNames.clear()
                                if (searchSOPCategoryList.isNotEmpty()) {
                                    searchSOPCategoryList.clear()
                                }
                                if(searchSopAttachmentlist.isNotEmpty())
                                {
                                    searchSopAttachmentlist.clear()
                                }
                                searchSopAttachmentlist.add(searchSOPList[i].sopAttachmentlist[j])
                                searchSOPCategoryList.add(
                                    GetSOPresponse(
                                        "",
                                        searchSopAttachmentlist,
                                        searchSOPList[i].type
                                    )
                                )
                            } else {
                                addedSOPNames.clear()
                                binding.txtnorecordfoundemployeesop.show()
                            }
                    }
                    setSOPAdapter(searchSOPCategoryList)
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Called after the text has been changed
                val newText = s.toString()
                // Perform any desired actions with the new text
            }
        })
    }

    private fun callPoliciesSOPApi() {
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getPoliciesSOP()
        }
    }

    private fun policySearchHandler() {
        val addedPolicyNames = HashSet<String>()
        binding.idSV.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.idSV.setHint("")
            false
        })

        binding.idSV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchPolicyAttachment.clear()
                // Called when the text is being changed
                if (s.toString() == null || s.toString().isEmpty()) {
                    searchPolicyAttachment.clear()
                    searchCategoryList.clear()
                    binding.txtnorecordfoundemployee.hide()
                    searchPolicyList = newPolicyDataList
                    setPolicyAdapter(searchPolicyList)

                } else {
                    addedPolicyNames.clear()
                    searchviewemployeeenable = true
                    searchCategoryList.clear()
                    for (i in searchPolicyList.indices)
                    for(j in searchPolicyList[i].attachmentlist.indices){

                        if ((searchPolicyList != null && searchPolicyList.get(i).name.lowercase().contains(s.toString().lowercase())))
                         {
                             if (addedPolicyNames.add(searchPolicyList[i].name)) {
                                 // SOP name not added before, add to searchSOPCategoryList
                                 binding.txtnorecordfoundemployeesop.hide()
                                 searchCategoryList.add(searchPolicyList[i])
                             }
                        }
                        else if(searchPolicyList[i].attachmentlist!=null && searchPolicyList[i].attachmentlist[j].title.lowercase().contains(s.toString().lowercase()))
                        {
                            searchCategoryList.clear()
                            searchPolicyAttachment.add(searchPolicyList[i].attachmentlist[j])
                            searchCategoryList.add(GetPoliciesresponse(searchPolicyAttachment,"",searchPolicyList[i].type))
                        }
                        else{
//                            searchCategoryList.clear()
                            addedPolicyNames.clear()
                            binding.txtnorecordfoundemployee.show()
                        }
                    }
                    setPolicyAdapter(searchCategoryList)

                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Called after the text has been changed
                val newText = s.toString()
                // Perform any desired actions with the new text
            }
        })
    }
    private fun setPolicyCategoryAdapter(policyCategory: ArrayList<GetPoliciesType>) {
       val adapter = PoliciesCategoriesAdapter(requireContext(),policyCategory,this)
        binding.policiestypeRecyclerView.adapter = adapter
    }
    private fun setPolicyAdapter(policylist: ArrayList<GetPoliciesresponse>) {
        if(policylist.isNotEmpty())
        {
            binding.rvpolicies.show()
            binding.txtnorecordfoundemployee.hide()
            val adapter =  PolicyAdapter(requireContext(),policylist,this)
            binding.rvpolicies.adapter = adapter
        }
        else{
            binding.rvpolicies.hide()
            binding.txtnorecordfoundemployee.show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        liveDataObserver()
    }

    private fun liveDataObserver() {
        homeViewModel.progressLoading.observe(viewLifecycleOwner, {
            showProgressDialog(it)
        })
        homeViewModel._getPoliciesSOP.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                   policiessopresponse  = response
                 initPolicyCategoryTypeAdapter(response)
                    initPolicyAdapter(response)
                  initSopCategoryTypeAdapter(response)
                  initSOPAdapter(response)
                }
                is NetworkResult.Error -> {
                }
                is NetworkResult.Loading -> {
                }
            }
        })
    }

    private fun initSOPAdapter(response: NetworkResult.Success<PoliciesSOPResponse>) {
        if(response.data?.body!=null)
        {
            if(response.data.body.getSOPresponse.isNullOrEmpty())
            {

            }
            else{
                var sopList = response.data.body.getSOPresponse
                if(sopslist.isNotEmpty())
                {
                    sopslist.clear()
                }
                sopslist.addAll(sopList)
                if(newSOPDataList.isNotEmpty())
                {
                    newSOPDataList.clear()
                }
                newSOPDataList.addAll(sopslist)
                if(searchSOPList.isNotEmpty())
                {
                    searchSOPList.clear()
                }
                searchSOPList.addAll(sopslist)
                setSOPAdapter(sopslist)
            }
        }
    }

    private fun setSOPAdapter(sopslist: ArrayList<GetSOPresponse>) {

        if(sopslist.isNotEmpty())
        {
            binding.rvSOP.show()
            binding.txtnorecordfoundemployeesop.hide()
            val adapter =  SOPAdapter(requireContext(),sopslist,this)
            binding.rvSOP.adapter = adapter
        }
        else{
            binding.rvSOP.hide()
            binding.txtnorecordfoundemployeesop.show()
        }
    }

    private fun initSopCategoryTypeAdapter(response: NetworkResult.Success<PoliciesSOPResponse>) {
        if(response.data?.body!=null)
        {
            if(response.data.body.getSOPSTypes.isNullOrEmpty())
            {

            }
            else{

                var sopsType = response.data.body.getSOPSTypes
                if(sopCategory.isNotEmpty())
                {
                    sopCategory.clear()
                }
                sopCategory.addAll(sopsType)
                setSOPTypeAdapter(sopCategory)

            }
        }
    }

    private fun setSOPTypeAdapter(sopCategory: ArrayList<GetSOPSType>) {
        sopCategory.forEach {
            if(it.name.lowercase().contains("all"))
            {
                binding.txtSOPType.text = it.name
            }
        }
        setSOPCategoryAdapter(sopCategory)
    }

    private fun sopAdapterHeight(sopCategory:ArrayList<GetSOPSType>) {
    if(sopCategory.size<3)
    {
        binding.mcvSOPCategory.layoutParams.height = 170
    }
        else
        {
        binding.mcvSOPCategory.layoutParams.height =  300
    }
    }


    private fun setSOPCategoryAdapter(sopCategory: ArrayList<GetSOPSType>) {
        sopAdapterHeight(sopCategory)
        val adapter = SOPCategoriesAdapter(requireContext(),sopCategory,this)
        binding.soptypeRecyclerView.adapter = adapter
    }

    private fun initPolicyAdapter(response: NetworkResult.Success<PoliciesSOPResponse>) {
        if(response.data?.body!=null)
        {
            if(response.data.body.getPoliciesresponse.isNullOrEmpty())
            {

            }
            else{
                var policylist = response.data.body.getPoliciesresponse
                if(policieslist.isNotEmpty())
                {
                    policieslist.clear()
                }
                policieslist.addAll(policylist)
                if(searchPolicyList.isNotEmpty())
                {
                    searchPolicyList.clear()
                }
                searchPolicyList.addAll(policieslist)
                setPolicyAdapter(policieslist)
            }
        }

    }

    private fun initPolicyCategoryTypeAdapter(response: NetworkResult.Success<PoliciesSOPResponse>) {
if(response.data?.body!=null)
{
    if(response.data.body.getPoliciesTypes.isNullOrEmpty())
    {

    }
    else{
        var policiestypes = response.data.body.getPoliciesTypes
        if(policyCategory.isNotEmpty())
        {
            policyCategory.clear()
        }
            policyCategory.addAll(policiestypes)

        setPolicyTypeAdapter(policyCategory)
    }
      }
    }

    private fun setPolicyTypeAdapter(policyCategory: ArrayList<GetPoliciesType>) {
        setPolicyAdapterHeight(policyCategory)
        policyCategory.forEach {
            if(it.name.lowercase().contains("all"))
            {
                binding.txtpolicy.text = it.name
            }
        }
        setPolicyCategoryAdapter(policyCategory)
    }

    private fun setPolicyAdapterHeight(policyCategory: ArrayList<GetPoliciesType>) {
      if(policyCategory.size>3)
      {
          binding.mcvPoliciesType.layoutParams.height = 170
      }
        else{
          binding.mcvPoliciesType.layoutParams.height = 250
      }
    }

    private fun onClickListeners() {
        binding.mcvPolicies.setOnClickListener {
            sessionmanagement?.selectedPDFViewTab("policies")
            binding.idSV.setText("")
            binding.idSV.setHint("Search")
            changePolicyLayout()
            if(binding.mcvPoliciesType.visibility == View.VISIBLE)
            {
                binding.mcvPoliciesType.visibility = View.GONE
            }
            policiessopresponse?.let { it1 -> initPolicyAdapter(it1) }
            policiessopresponse?.let { it1 -> initPolicyCategoryTypeAdapter(it1) }

        }
        binding.mcvSOP.setOnClickListener {
            sessionmanagement?.selectedPDFViewTab("sop")

            if(binding.mcvSOPCategory.visibility == View.VISIBLE)
            {
                binding.mcvSOPCategory.visibility = View.GONE
            }
            binding.idSVSOP.setText("")
            binding.idSVSOP.setHint("Search")
            policiessopresponse?.let { it1 -> initSOPAdapter(it1) }
            policiessopresponse?.let { it1 -> initSopCategoryTypeAdapter(it1) }
            changeSOPLayout()
        }
       binding.mcvPolicyType.setOnClickListener {
           changePolicyLayoutVisibillity()
       }
        binding.mcvSOPType.setOnClickListener {
            changeSOPLayoutVisibility()
        }


    }

    private fun changeSOPLayoutVisibility() {
        if(binding.mcvSOPCategory.visibility == View.VISIBLE)
        {
            binding.mcvSOPCategory.visibility = View.GONE
        }
        else{
            binding.mcvSOPCategory.visibility = View.VISIBLE
        }
    }



    private fun changePolicyLayoutVisibillity() {
        if(binding.mcvPoliciesType.visibility == View.VISIBLE)
        {
            binding.mcvPoliciesType.visibility = View.GONE
        }
        else{
            binding.mcvPoliciesType.visibility = View.VISIBLE
        }
    }

    private fun changeSOPLayout() {
        binding.mcvSOPView.show()
        binding.mcvPoliciesView.hide()
        binding.mcvSOP.setCardBackgroundColor(resources.getColor(R.color.charcoal))
        binding.mcvSOP.setStrokeColor(resources.getColor(R.color.yellow))
        binding.mcvSOP.strokeWidth = 1
        binding.txtpolicies.setTextColor(resources.getColor(R.color.white))
        binding.txtSOP.setTextColor(resources.getColor(R.color.yellow))
        binding.mcvPolicies.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.mcvPolicies.strokeWidth = 0
    }

    private fun changePolicyLayout() {
        binding.mcvPoliciesView.show()
        binding.mcvSOPView.hide()
        binding.mcvPolicies.setCardBackgroundColor(resources.getColor(R.color.charcoal))
        binding.mcvPolicies.setStrokeColor(resources.getColor(R.color.yellow))
        binding.mcvPolicies.strokeWidth = 1
        binding.txtSOP.setTextColor(resources.getColor(R.color.white))
        binding.txtpolicies.setTextColor(resources.getColor(R.color.yellow))
        binding.mcvSOP.setCardBackgroundColor(resources.getColor(R.color.dark_grey))
        binding.mcvSOP.strokeWidth = 0
    }



    override fun onclick(value: Boolean) {
    }

    override fun onLocationPermission(locationvalue: Boolean)
    {

    }

        override fun onPolicyTypeClick(position: Int) {
            var showPolicyType:Boolean = false
            if(newPolicyDataList.isNotEmpty())
        {
            newPolicyDataList.clear()
        }
        binding.mcvPoliciesType.hide()
         policycategoryType = policyCategory.get(position).name
        binding.txtpolicy.text = policycategoryType
        if(policycategoryType.lowercase().contains("all"))
        {
            showPolicyType = true
            newPolicyDataList.addAll(policieslist)
            searchPolicyList = newPolicyDataList
        }
        else{

            policieslist.forEach {
                if(policycategoryType == it.name)
                {
                    showPolicyType = false
                    newPolicyDataList.add(GetPoliciesresponse(it.attachmentlist,it.name,it.type))
                }
            }
            searchPolicyList = newPolicyDataList
        }
        setPolicyAdapter(newPolicyDataList)
    }

    override fun onItemPolicyClick(url: String, pdfname: String) {
        try{
            val pdfdata = PDFInfo(pdfurl = url, pdfName = pdfname)
            findNavController().navigate(PoliciesFragmentDirections.actionPoliciesfragmentToPdfViewFragment(pdfdata))
        }
        catch (e:Exception)
        {
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel._getPoliciesSOP.postValue(null)
    }
    override fun onResume() {
        super.onResume()
        binding.idSV.setText("")
        binding.idSV.setHint("Search")
        binding.idSVSOP.setText("")
        binding.idSVSOP.setHint("Search")
    }



    override fun onSOPTypeClick(position: Int) {
        var showSOPType:Boolean = false
        if(newSOPDataList.isNotEmpty())
        {
            newSOPDataList.clear()
        }
        binding.mcvSOPCategory.hide()
         sopcategoryType = sopCategory.get(position).name
        binding.txtSOPType.text = sopcategoryType
        if(sopcategoryType.lowercase().contains("all"))
        {
            showSOPType = true
            newSOPDataList.addAll(sopslist)
            searchSOPList = newSOPDataList
        }
        else{
            sopslist.forEach {
                if(sopcategoryType == it.name)
                {
                    showSOPType = false
                    newSOPDataList.add(GetSOPresponse(it.name,it.sopAttachmentlist,it.type))
                }
            }
            searchSOPList = newSOPDataList
        }
        setSOPAdapter(newSOPDataList)
    }

    override fun onItemSOPClick(url: String, name: String) {
        try{
            val pdfdata = PDFInfo(pdfurl = url, pdfName = name)
            findNavController().navigate(PoliciesFragmentDirections.actionPoliciesfragmentToPdfViewFragment(pdfdata))
        }
        catch (e:Exception)
        {
        }
    }
}
