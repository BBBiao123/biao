 public void SendSms() {
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                List<BasicNameValuePair> formparams = new ArrayList<>();
                formparams.add(new BasicNameValuePair("Account","�˺�"));
			    formparams.add(new BasicNameValuePair("Pwd","�ӿ���Կ"));//��¼��̨ ��ҳ��ʾ
			    formparams.add(new BasicNameValuePair("Content","������֤����1234"));
			    formparams.add(new BasicNameValuePair("Mobile","138****1234"));
			    formparams.add(new BasicNameValuePair("SignId","ǩ��id"));//��¼��̨ ���ǩ����ȡid	
			   
                HttpPost httpPost = new HttpPost("http://api.feige.ee/SmsService/Send");
                httpPost.setEntity(new UrlEncodedFormEntity(formparams,"UTF-8"));
                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }