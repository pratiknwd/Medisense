import pandas as pd
from userdefined_functions.report_scanner import report_scanner
from PIL import Image
import streamlit as st
import json

def upload_report():
    st.subheader("Medisense")
    image_file = st.file_uploader("Upload a file", type=["png", "jpg", "jpeg"])
    if image_file:
        st.success("Image uploaded successfully")
        col1, col2 = st.columns(2)
        with col1:
            st.image(image_file)
        with col2:
            result = report_scanner.report_scan(image_file)
            # st.write(result)
            st.text_area("Extracted report", result, height=600)
        json_result = report_scanner.StringtoJson(result)
        if json_result:
            df = pd.DataFrame(json_result)
            st.dataframe(df)
        #     report = report_scanner.report_analyze(df)
        # if report:
        #     st.write(report)

def main():
    upload_report()


if __name__ == "__main__":
    main()